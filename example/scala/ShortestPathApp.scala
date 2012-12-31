import sabre.algorithm._
import sabre.system.Sabre
import sabre.util.NodeGetter
import scala.annotation.switch
import scala.Console.err
import scala.collection.mutable
import scala.io.Source
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._

object ShortestPathAlgorithm extends AbstractAlgorithm {
  def getFullBfs(graph: Graph[Int, UnDiEdge], u: Int): Map[Int, Long] = {
    val bfs = mutable.Map.empty[Int, Long]
    val queue = mutable.Queue.empty[graph.NodeT]

    graph.nodes.foreach(node => bfs(node) = -1)
    bfs(u) = 0

    queue += graph.get(u)

    while (queue.nonEmpty) {
      val v = queue.dequeue()
      v.neighbors.foreach { vNeighbor =>
        if (bfs(vNeighbor) == -1) {
          bfs(vNeighbor) = bfs(v) + 1
          queue += vNeighbor
        }
      }
    }

    bfs.toMap
  }

  override def execute(graph: Graph[Int, UnDiEdge], input: Any): Option[AbstractResult] = input match {
    case u: Int =>
      val uNode = graph find u
      uNode match {
        case None => None
        case _ => Some(MapResult(input, getFullBfs(graph, u)))
      }
    case _ => None
  }
}

object ShortestPathApp {
  def main(args: Array[String]): Unit = {
    if (args.size == 0)
      Sabre.execute(ShortestPathAlgorithm, NodeGetter.getAllNodes())
    else Sabre.execute(ShortestPathAlgorithm, NodeGetter.getAllNodes(), args(0))
  }
}