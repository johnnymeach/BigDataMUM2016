package cs.mum.edu.util
/**
  * Created by Sokly on 6/10/16.
  */
import scala.math.Ordering
object OrderingUtils {
  object SecondValueOrdering extends Ordering[(String, Int)] {
    def compare(a: (String, Int), b: (String, Int)) = {
      a._2 compare b._2
    }
  }

  object SecondValueLongOrdering extends Ordering[(String, Long)] {
    def compare(a: (String, Long), b: (String, Long)) = {
      a._2 compare b._2
    }
  }
  object SecondValuePairOrdering extends Ordering[((String,Int), Int)] {
    def compare(a: ((String,Int), Int), b: ((String,Int), Int)) = {
      a._2 compare b._2
    }
  }
}
