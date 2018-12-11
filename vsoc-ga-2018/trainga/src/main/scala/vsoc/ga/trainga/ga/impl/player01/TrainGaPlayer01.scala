package vsoc.ga.trainga.ga.impl.player01

import org.slf4j.LoggerFactory
import vsoc.ga.matches.Team
import vsoc.ga.trainga.ga.TrainGa
import vsoc.ga.trainga.ga.impl.team01.TrainGaAbstract

class TrainGaPlayer01 extends TrainGa[DataPlayer01] {

  private val log = LoggerFactory.getLogger(classOf[TrainGaAbstract])

  override def id: String = "trainGaPlayer01"

  override def teamsFromPopulation: Seq[Team] = ???

  var cnt = 0;

  override def run(trainGaId: String, trainGaNr: String): Unit = {
    log.info(s"start TrainGaPlayer01")
    var score = DataPlayer01()
    try {
      if (population.isEmpty) {
        population = createInitialPopGeno
      }
      while (true) {
        Thread.sleep(1000)

        iterations += 1
        score = score.copy(iterations = iterations)
        listeners.foreach(l => l.onIterationFinished(iterations, Some(score)))
      }
    } catch {
      case e: Exception =>
        val msg = s"Error running $trainGaId $trainGaNr ${e.getMessage}"
        log.error(msg, e)
    }
  }

  override def fullDesc: String = "TrainGaPlayer01"

  def createInitialPopGeno: Seq[Seq[Double]] = Seq(Seq.empty[Double])

}
