package u05lab.code

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.{BeforeAll, Test}
import u05lab.code.ExamsManager.{ExamManagerImpl, ExamResultFactoryImpl, Kind}


class ExamsManagerTest {

  def prepareExams(): Unit ={
    ExamManagerImpl.createNewCall("gennaio")
    ExamManagerImpl.createNewCall("febbraio")
    ExamManagerImpl.createNewCall("marzo")

    ExamManagerImpl.addStudentResult("gennaio", "rossi", ExamResultFactoryImpl.failed) // rossi -> fallito
    ExamManagerImpl.addStudentResult("gennaio", "bianchi", ExamResultFactoryImpl.retired) // bianchi -> ritirato
    ExamManagerImpl.addStudentResult("gennaio", "verdi", ExamResultFactoryImpl.succeeded(28)) // verdi -> 28
    ExamManagerImpl.addStudentResult("gennaio", "neri", ExamResultFactoryImpl.succeededCumLaude) // neri -> 30L

    ExamManagerImpl.addStudentResult("febbraio", "rossi", ExamResultFactoryImpl.failed); // etc..
    ExamManagerImpl.addStudentResult("febbraio", "bianchi", ExamResultFactoryImpl.succeeded(20))
    ExamManagerImpl.addStudentResult("febbraio", "verdi", ExamResultFactoryImpl.succeeded(30))

    ExamManagerImpl.addStudentResult("marzo", "rossi", ExamResultFactoryImpl.succeeded(25))
    ExamManagerImpl.addStudentResult("marzo", "bianchi", ExamResultFactoryImpl.succeeded(25))
    ExamManagerImpl.addStudentResult("marzo", "viola", ExamResultFactoryImpl.failed)

  }

  @Test
  def testExamResultsBasicBehaviour(): Unit = {
    prepareExams()
    // esame fallito, non c'è voto
    assertEquals(ExamResultFactoryImpl.failed.kind, Kind.FAILED)
    assertFalse(ExamResultFactoryImpl.failed.evaluation.isDefined)
    assertFalse(ExamResultFactoryImpl.failed.cumLaude)
    assertEquals(ExamResultFactoryImpl.failed.toString, "FAILED")

    // lo studente si è ritirato, non c'è voto
    assertEquals(ExamResultFactoryImpl.retired.kind, Kind.RETIRED)
    assertFalse(ExamResultFactoryImpl.retired.evaluation.isDefined)
    assertFalse(ExamResultFactoryImpl.retired.cumLaude)
    assertEquals(ExamResultFactoryImpl.retired.toString, "RETIRED")

    // 30L
    assertEquals(ExamResultFactoryImpl.succeededCumLaude.kind, Kind.SUCCEEDED)
    assertEquals(ExamResultFactoryImpl.succeededCumLaude.evaluation, Some(30))
    assertTrue(ExamResultFactoryImpl.succeededCumLaude.cumLaude)
    assertEquals(ExamResultFactoryImpl.succeededCumLaude.toString, "SUCCEEDED(30L)")

    // esame superato, ma non con lode
    assertEquals(ExamResultFactoryImpl.succeeded(28).kind, Kind.SUCCEEDED)
    assertEquals(ExamResultFactoryImpl.succeeded(28).evaluation, Some(28))
    assertFalse(ExamResultFactoryImpl.succeeded(28).cumLaude)
    assertEquals(ExamResultFactoryImpl.succeeded(28).toString, "SUCCEEDED(28)")
  }

  @Test
  def testExamManagement(): Unit = {
    prepareExams()
    // partecipanti agli appelli di gennaio e marzo
    assertEquals(ExamManagerImpl.studentsFromCall("gennaio"), Set("rossi","bianchi","verdi","neri"))
    assertEquals(ExamManagerImpl.studentsFromCall("marzo"),Set("rossi","bianchi","viola"))

    // promossi di gennaio con voto
    assertEquals(2, ExamManagerImpl.evaluationsMapFromCall("gennaio").size)
    assertEquals(28, ExamManagerImpl.evaluationsMapFromCall("gennaio")("verdi"))
    assertEquals(30, ExamManagerImpl.evaluationsMapFromCall("gennaio")("neri"))
    // promossi di febbraio con voto
    assertEquals(2, ExamManagerImpl.evaluationsMapFromCall("febbraio").size)
    assertEquals(20, ExamManagerImpl.evaluationsMapFromCall("febbraio")("bianchi"))
    assertEquals(30, ExamManagerImpl.evaluationsMapFromCall("febbraio")("verdi"))

    // tutti i risultati di rossi (attenzione ai toString!!)
    assertEquals(3, ExamManagerImpl.resultsMapFromStudent("rossi").size)
    assertEquals("FAILED", ExamManagerImpl.resultsMapFromStudent("rossi")("gennaio"))
    assertEquals("FAILED", ExamManagerImpl.resultsMapFromStudent("rossi")("febbraio"))
    assertEquals("SUCCEEDED(25)", ExamManagerImpl.resultsMapFromStudent("rossi")("marzo"))
    // tutti i risultati di bianchi
    assertEquals(3, ExamManagerImpl.resultsMapFromStudent("bianchi").size)
    assertEquals("RETIRED", ExamManagerImpl.resultsMapFromStudent("bianchi")("gennaio"))
    assertEquals("SUCCEEDED(20)", ExamManagerImpl.resultsMapFromStudent("bianchi")("febbraio"))
    assertEquals("SUCCEEDED(25)", ExamManagerImpl.resultsMapFromStudent("bianchi")("marzo"))
    // tutti i risultati di neri
    assertEquals(1, ExamManagerImpl.resultsMapFromStudent("neri").size)
    assertEquals("SUCCEEDED(30L)", ExamManagerImpl.resultsMapFromStudent("neri")("gennaio"))
  }

  @Test
  def  testExamsManagement(): Unit = {
    prepareExams()
    // miglior voto acquisito da ogni studente, o vuoto..
    assertEquals(Option(25), ExamManagerImpl.bestResultFromStudent("rossi"))
    assertEquals(Option(30), ExamManagerImpl.bestResultFromStudent("neri"))
    assertEquals(Option.empty, ExamManagerImpl.bestResultFromStudent("viola"))
  }
}