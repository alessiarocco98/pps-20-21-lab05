package u05lab.code

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

class SomeTest {

  @Test
  def testIncremental() {
    val l = List("a", "b", "c")
    val l2 = List(1,2,3, 5, 4, 6, 7)
    val emptyList = List.nil[Int]

    // Ex. 1: zipRight
    assertEquals(List.nil, List.nil.zipRight)
    assertEquals(List(("a", 0), ("b", 1), ("c",2)), l.zipRight)

    // Ex. 2: partition
    assertEquals((List("a"), List("b", "c")), l.partition(_ == "a"))

    // Ex. 3: span
    assertEquals((List(1,2,3), List(5,4,6,7)), l2.span(_ <= 4))

    // Ex. 4: reduce
    assertEquals(28, l2.reduce(_ + _))
    assertThrows(classOf[UnsupportedOperationException],() => emptyList.reduce(_+_))

    // Ex. 5: takeRight
    assertEquals(List(4,6,7), l2.takeRight(3))
  }
}