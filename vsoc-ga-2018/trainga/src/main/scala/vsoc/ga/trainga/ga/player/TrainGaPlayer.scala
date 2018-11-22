package vsoc.ga.trainga.ga.player

import vsoc.ga.common.data.Data02
import vsoc.ga.genetic._
import vsoc.ga.matches.{Match, MatchResult, Matches, Team}
import vsoc.ga.trainga.ga.TrainGa

import scala.util.Random

class TrainGaPlayer extends TrainGa[Data02] {

  val matchSteps = 20000


  val tester: PhenoTester[PlayerPheno, Data02] = createTester

  val selStrategy: SelectionStrategy[Double] = createSelStrategy

  val transformer: Transformer[Double, PlayerPheno] = createTransformer

  override def id: String = "TrainGaPlayer"

  override def fullDesc: String = "TrainGaPlayer"

  override def teamsFromGeno(geno: Seq[Geno[Double]]): Seq[Team] = ???

  override def run(trainGaId: String, trainGaNr: String): Unit = {

    val ga = new GA(tester, selStrategy, transformer)
    var gaResult = initialGaResult
    while (true) {
      gaResult = ga.nextPopulation(gaResult.newPopulation)
    }
  }

  def initialGaResult: GAResult[Data02, Double] = {

    def initialPopGeno: PopGeno[Double] = ???

    new GAResult[Data02, Double] {
      override def score: Option[Data02] = None

      override def newPopulation: PopGeno[Double] = initialPopGeno
    }
  }

  def createTester: PhenoTester[PlayerPheno, Data02] = {

    new PhenoTester[PlayerPheno, Data02]() {

      case class PhenoTested(score: Data02, player: PlayerPheno)

      def createTeam: Team = ???

      override def test(phenos: Seq[PlayerPheno]): PhenoTesterResult[PlayerPheno, Data02] = {

        var phenosTested: Seq[PhenoTested] = phenos.map(p => PhenoTested(Data02(), p))


        val t1 = createTeam
        val t2 = createTeam
        val m: Match = Matches.of(t1, t2)
        for (_ <- 1 to matchSteps) m.takeStep()
        val matchResult: MatchResult = m.state
        val eastResult = matchResult.teamEastResult
        val westResult = matchResult.teamWestResult

        ???

      }

      override def fullDesc: String = "Player:PhenoTester"
    }


  }

  def createSelStrategy: SelectionStrategy[Double] = ???

  def createTransformer: Transformer[Double, PlayerPheno] = ???


}


/**
  * Selects players for a team
  * In order to create some kinds of player roles player 1
  * is selected from the first third of population, player 2
  * is selcted from the second third of the population and
  * player 3 from the third third.
  * To understand this have a look at the testcases.
  */
class PlayerIndexCreator(popSize: Int) {

  val ms = math.floor(popSize.toDouble / 3).toInt
  val diff = popSize - (ms * 3)
  val (amin, amax, bmin, bmax, cmin, cmax) =
    if (diff == 0)
      (0, ms - 1, ms, 2 * ms - 1, 2 * ms, 3 * ms - 1)
    else if (diff == 1)
      (0, ms, ms + 1, 2 * ms, 2 * ms + 1, 3 * ms)
    else
      (0, ms, ms + 1, 2 * ms + 1, 2 * ms + 2, 3 * ms + 1)

  def ran: (Int, Int, Int) = {
    (amin + Random.nextInt(amax - amin + 1),
      bmin + Random.nextInt(bmax - bmin + 1),
      cmin + Random.nextInt(cmax - cmin + 1))
  }

}

