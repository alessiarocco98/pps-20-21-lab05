package u05lab.code

import u05lab.code.ExamsManager.Kind.Kind

import scala.collection.MapView

object ExamsManager extends App {

  /* See: https://bitbucket.org/mviroli/oop2018-esami/src/master/a01b/e1/Test.java */
  object Kind extends Enumeration {
    type Kind = Value
    val RETIRED, FAILED, SUCCEEDED = Value
  }

  trait ExamResult {
    def kind: Kind
    def evaluation: Option[Int]
    def cumLaude: Boolean
  }

  case class ExamResultImpl(override val kind: Kind,
                            override val evaluation: Option[Int] = None,
                            override val cumLaude: Boolean = false) extends ExamResult {
    override def toString: String = evaluation match {
      case None => kind.toString
      case Some(value) if cumLaude => kind.toString().concat("(30L)")
      case _ => kind.toString.concat("(" + evaluation.get + ")")
    }

  }

  trait ExamResultFactory {
    def failed: ExamResult
    def retired: ExamResult
    def succeeded(evaluation: Int): ExamResult
    def succeededCumLaude: ExamResult
  }

  object ExamResultFactoryImpl extends ExamResultFactory {
    override def failed: ExamResult = ExamResultImpl(Kind.FAILED)

    override def retired: ExamResult = ExamResultImpl(Kind.RETIRED)

    override def succeeded(evaluation: Int): ExamResult = ExamResultImpl(Kind.SUCCEEDED, Some(evaluation))

    override def succeededCumLaude: ExamResult = ExamResultImpl(Kind.SUCCEEDED, Some(30), true)
  }

  trait ExamManager {
    def createNewCall(call: String): Unit
    def addStudentResult(call: String, student: String, result: ExamResult): Unit
    def studentsFromCall(call: String): Set[String]
    def evaluationsMapFromCall(call: String): MapView[String, Int]
    def resultsMapFromStudent(student: String): Map[String, String]
    def bestResultFromStudent(student: String): Option[Int]
  }
 
  object ExamManagerImpl extends ExamManager {
    var mapCalls: Map[String, Map[String, ExamResult]] = Map.empty

    override def createNewCall(call: String): Unit = {
      mapCalls = mapCalls + (call -> Map.empty)
    }

    override def addStudentResult(call: String, student: String, result: ExamResult): Unit ={
      val map = mapCalls.get(call).get + (student -> result)
      mapCalls = mapCalls + (call -> map)
    }

    override def studentsFromCall(call: String): Set[String] = {
      mapCalls.get(call).get.keySet
    }

    override def evaluationsMapFromCall(call: String): MapView[String, Int] = {
      mapCalls(call).filter(m => m._2.evaluation.isDefined).mapValues(er => er.evaluation.get)
    }

    override def resultsMapFromStudent(student: String): Map[String, String] =
      mapCalls.filter(s=>s._2.contains(student)).map(s => s._1 -> s._2(student).toString)

    override def bestResultFromStudent(student: String): Option[Int] = {
      if(mapCalls.filter(s=>s._2.contains(student) && s._2(student).evaluation.isDefined).size > 0)
        Option(mapCalls.filter(s=>s._2.contains(student) && s._2(student).evaluation.isDefined).map(s=> s._2(student).evaluation.get).max)
      else
        Option.empty
    }
  }
}