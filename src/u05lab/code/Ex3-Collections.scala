package u05lab.code

import java.util.concurrent.TimeUnit
import scala.collection.immutable.HashSet
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.FiniteDuration

object PerformanceUtils {
  case class MeasurementResults[T](result: T, duration: FiniteDuration) extends Ordered[MeasurementResults[_]] {
    override def compare(that: MeasurementResults[_]): Int = duration.toNanos.compareTo(that.duration.toNanos)
  }

  def measure[T](msg: String)(expr: => T): MeasurementResults[T] = {
    val startTime = System.nanoTime()
    val res = expr
    val duration = FiniteDuration(System.nanoTime()-startTime, TimeUnit.NANOSECONDS)
    if(!msg.isEmpty) println(msg + " -- " + duration.toNanos + " nanos; " + duration.toMillis + "ms")
    MeasurementResults(res, duration)
  }

  def measure[T](expr: => T): MeasurementResults[T] = measure("")(expr)
}

import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer

object CollectionsTest extends App {

  /* Linear sequences: List, ListBuffer */
  println("List: ")
  val list: List[Int] = List(10,20,30,40,50)
  println(list.length) //5
  println(list(1)) //10
  println(5 :: list) //List(5,10,20,30,40,50)
  println(list.filter(_ != 40)) //List(10,20,30,50)

  println("ListBuffer: ")
  val listBuffer: ListBuffer[Int] = ListBuffer(10,20,30,40,50)
  println(listBuffer.length) //5
  println(listBuffer.last) //50
  println(listBuffer.addOne(5)) //ListBuffer(10, 20, 30, 40, 50, 5)
  println(listBuffer.-=(40)) //ListBuffer(10, 20, 30, 50, 5)

  /* Indexed sequences: Vector, Array, ArrayBuffer */
  println("Vector: ")
  val vector: Vector[Int] = Vector(10,20,30,40,50)
  println(vector.length) //5
  println(vector.last) //50
  println(vector.toVector) // Vector(10,20,30,40,50)
  println(vector.updated(2,5)) // Vector(10,20,5,40,50)
  println(vector.drop(1)) // Vector(20,30,40,50)

  println("Array: ")
  val array: Array[Int] = Array(10,20,30,40,50)
  println(array.length) //5
  println(array.max) //50
  array.update(0,1)
  println(array.toList) //List(1, 20, 30, 40, 50)
  println(array.drop(1).toList) //List(20, 30, 40, 50)

  println("ArrayBuffer: ")
  val arrayBuffer: ArrayBuffer[Int] = ArrayBuffer(10,20,30,40,50)
  println(arrayBuffer.length) //5
  println(arrayBuffer.addOne(5)) //ArrayBuffer(10, 20, 30, 40, 50, 5)
  arrayBuffer.update(3,4)
  println(arrayBuffer) //ArrayBuffer(10, 20, 30, 4, 50, 5)
  arrayBuffer.remove(2,1)
  println(arrayBuffer) //ArrayBuffer(10, 20, 4, 50, 5)

  /* Sets */
  //Immutable Set
  println("Immutable.HashSet: ")
  val hashSet = Set(10,20,30,40,50)
  println(hashSet.size) //5
  println(hashSet + 60) //HashSet(10, 20, 60, 50, 40, 30)
  println(hashSet.diff(Set(10))) //HashSet(10, 20, 50, 40)
  println(hashSet - 30 ) //HashSet(10, 20, 50, 40)
  //Immutable Set
  println("Mutable.HashSet: ")
  val mutableHashSet = mutable.HashSet[Int](10,20,30,40,50)
  println(mutableHashSet.size) //5
  mutableHashSet.addOne(60)
  println(mutableHashSet) //HashSet(50, 20, 40, 10, 60, 30)
  mutableHashSet.update(10, false)
  println(mutableHashSet) //HashSet(50, 20, 40, 60, 30)
  mutableHashSet.remove(20)
  println(mutableHashSet) //HashSet(50, 40, 60, 30)

  /* Maps */
  println("Immutable.Map: ")
  val m: Map[Int,String]= Map(10->"a", 20 ->"b", 30 ->"c")
  println(m.size) //3
  println(m + (40 -> "d")) //Map(10 -> a, 20 -> b, 30 -> c, 40 -> d)
  println(m - 10) //Map(20 -> b, 30 -> c)

  println("Mutable.Map: ")
  val mm: mutable.Map[Int, String] = mutable.Map(10->"a", 20 ->"b", 30 ->"c")
  println(mm.size) //3
  println(mm += 40 -> "d") //HashMap(40 -> d, 10 -> a, 20 -> b, 30 -> c)
  mm.update(40, "dd")
  println(mm) //HashMap(40 -> dd, 10 -> a, 20 -> b, 30 -> c)
  println(mm -= 40) //HashMap(10 -> a, 20 -> b, 30 -> c)

  /* Comparison */
  import PerformanceUtils._
  val lst = (1 to 1000000).toList
  val vec = (1 to 1000000).toVector
  assert( measure("lst last"){ lst.last } > measure("vec last"){ vec.last } )
}