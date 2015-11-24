package scala.coroutines



import scala.collection._
import scala.coroutines.common._
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context



/** Generates control flow graphs, and converts CFG nodes to ASTs.
 */
trait ControlFlowGraph[C <: Context] {
  self: Analyzer[C] =>

  val c: C

  import c.universe._

  private sealed trait CanEmit

  private object Permissions {
    implicit object canEmit extends CanEmit
  }

  abstract class Node {
    var successors: List[Node] = Nil

    val uid: Long

    val tree: Tree

    def chain: Chain

    def copyWithoutSuccessors: Node

    final def emitCode(z: Zipper, subgraph: Subgraph)(implicit t: Table): Zipper = {
      val seen = mutable.Set[Node]()
      this.markEmit(z, seen, subgraph)
    }

    final def markEmit(
      z: Zipper, seen: mutable.Set[Node], subgraph: Subgraph
    )(implicit t: Table): Zipper = {
      import Permissions.canEmit
      if (!seen(this)) {
        seen += this
        this.emit(z, seen, subgraph)
      } else z
    }

    def emit(
      z: Zipper, seen: mutable.Set[Node], subgraph: Subgraph
    )(implicit ce: CanEmit, t: Table): Zipper

    def prettyPrint = {
      val text = new StringBuilder
      var count = 0
      val seen = mutable.Map[Node, Int]()
      def emit(n: Node, prefix: String) {
        def shorten(s: String) = {
          if (s.contains('\n')) s.takeWhile(_ != '\n') + "..." else s
        }
        seen(n) = count
        val treerepr = shorten(n.tree.toString)
        text.append(s"$prefix|-> $count: Node($treerepr)\n")
        count += 1
        def emitChild(c: Node, newPrefix: String) {
          if (seen.contains(c)) {
            text.append(s"$newPrefix|-> label ${seen(c)}")
          } else {
            emit(c, newPrefix)
          }
        }
        if (n.successors.nonEmpty) {
          for (s <- n.successors.tail) {
            emitChild(s, prefix + "|   ")
          }
          emitChild(n.successors.head, prefix)
        }
      }
      emit(this, "")
      text.toString
    }
  }

  object Node {
    class If(val tree: Tree, val chain: Chain, val uid: Long) extends Node {
      def emit(
        z: Zipper, seen: mutable.Set[Node], subgraph: Subgraph
      )(implicit ce: CanEmit, table: Table): Zipper = {
        val q"if ($cond) $_ else $_" = tree
        val newZipper = Zipper(null, Nil, trees => q"..$trees")
        val elsenode = this.successors(1)
        val thennode = this.successors(0)
        val elsebranch = elsenode.markEmit(newZipper, seen, subgraph).root.result
        val thenbranch = thennode.markEmit(newZipper, seen, subgraph).root.result
        val untypedcond = table.untyper.untypecheck(cond)
        val iftree = q"if ($untypedcond) $thenbranch else $elsebranch"
        z.append(iftree)
      }
      def copyWithoutSuccessors = new If(tree, chain, uid)
    }

    class IfMerge(val tree: Tree, val chain: Chain, val uid: Long) extends Node {
      def emit(
        z: Zipper, seen: mutable.Set[Node], subgraph: Subgraph
      )(implicit ce: CanEmit, table: Table): Zipper = {
        if (successors.length == 1) {
          successors.head.markEmit(z, seen, subgraph)
        } else if (successors.length == 0) {
          // do nothing
          z
        } else sys.error(s"Multiple successors for <$tree>.")
      }
      def copyWithoutSuccessors = new IfMerge(tree, chain, uid)
    }

    class YieldVal(val tree: Tree, val chain: Chain, val uid: Long) extends Node {
      def emit(
        z: Zipper, seen: mutable.Set[Node], subgraph: Subgraph
      )(implicit ce: CanEmit, table: Table): Zipper = {
        val q"coroutines.this.`package`.yieldval[$_]($x)" = tree
        // store state for non-val variables in scope
        val stacksets = for {
          (sym, info) <- chain.allvars
          if subgraph.mustStoreVar(sym)
        } yield {
          val cparam = table.names.coroutineParam
          val stack = info.stackname
          val pos = info.stackpos
          val encodedval = info.encodeLong(q"${info.name}")
          q"scala.coroutines.common.Stack.set($cparam.$stack, $pos, $encodedval)"
        }
        // store return value
        val termtree = q"""
          ..${stacksets.toList}
          ${table.names.coroutineParam}.result = ${table.untyper.untypecheck(x)}
          return
        """
        z.append(termtree)
      }
      def copyWithoutSuccessors = new YieldVal(tree, chain, uid)
    }

    class Statement(val tree: Tree, val chain: Chain, val uid: Long) extends Node {
      def emit(
        z: Zipper, seen: mutable.Set[Node], subgraph: Subgraph
      )(implicit ce: CanEmit, table: Table): Zipper = {
        // inside the control-flow-construct, normal statement
        val z1 = z.append(table.untyper.untypecheck(tree))
        if (successors.length == 1) {
          successors.head.markEmit(z1, seen, subgraph)
        } else if (successors.length == 0) {
          // do nothing
          z1
        } else sys.error(s"Multiple successors for <$tree>.")
      }
      def copyWithoutSuccessors = new Statement(tree, chain, uid)
    }
  }

  class Subgraph {
    val referencedVars = mutable.LinkedHashMap[Symbol, VarInfo]()
    val declaredVars = mutable.LinkedHashMap[Symbol, VarInfo]()
    val exitPoints = mutable.LinkedHashMap[Node, Long]()
    var start: Node = _
    def usesVar(sym: Symbol) = referencedVars.contains(sym)
    def declaresVar(sym: Symbol) = declaredVars.contains(sym)
    def mustStoreVar(sym: Symbol) = {
      usesVar(sym) && (sym.asTerm.isVar || declaresVar(sym))
    }
  }

  def generateControlFlowGraph()(implicit table: Table): Node = {
    def traverse(t: Tree, c: Chain): (Node, Node) = {
      t match {
        case q"$_ val $name: $_ = $_" =>
          c.addVar(t, name, false)
          val n = new Node.Statement(t, c, table.newNodeUid())
          (n, n)
        case q"$_ var $name: $_ = $_" =>
          c.addVar(t, name, false)
          val n = new Node.Statement(t, c, table.newNodeUid())
          (n, n)
        case q"coroutines.this.`package`.yieldval[$_]($_)" =>
          val n = new Node.YieldVal(t, c, table.newNodeUid())
          (n, n)
        case q"if ($cond) $thenbranch else $elsebranch" =>
          val ifnode = new Node.If(t, c, table.newNodeUid())
          val mergenode = new Node.IfMerge(q"{}", c, table.newNodeUid())
          def addBranch(branch: Tree) {
            val nestedchain = c.newChain(t)
            val (childhead, childlast) = traverse(branch, nestedchain)
            ifnode.successors ::= childhead
            childlast.successors ::= mergenode
          }
          addBranch(thenbranch)
          addBranch(elsebranch)
          (ifnode, mergenode)
        case q"{ ..$stats }" if stats.nonEmpty && stats.tail.nonEmpty =>
          val nestedchain = c.newChain(t)
          val (first, childlast) = traverse(stats.head, nestedchain)
          var current = childlast
          for (stat <- stats.tail) {
            val (childhead, childlast) = traverse(stat, nestedchain)
            current.successors ::= childhead
            current = childlast
          }
          (first, current)
        case _ =>
          val n = new Node.Statement(t, c, table.newNodeUid())
          (n, n)
      }
    }

    val lambda = table.lambda
    val (args, body) = lambda match {
      case q"(..$args) => $body" => (args, body)
      case _ => c.abort(lambda.pos, "The coroutine takes a single function literal.")
    }

    for (t <- args) {
      val q"$_ val $name: $_ = $_" = t
      table.topChain.addVar(t, name, true)
    }

    // traverse tree to construct CFG and extract local variables
    val (head, last) = traverse(body, table.topChain)
    println(head.prettyPrint)
    head
  }
}