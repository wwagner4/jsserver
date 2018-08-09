package vsoc.ga.trainga.ga

import vsoc.ga.common.data.Data02
import vsoc.ga.trainga.behav.{InputMapperNn, OutputMapperNn}
import vsoc.ga.trainga.ga.impl._
import vsoc.ga.trainga.nn.{NeuralNet, NeuralNets}

object TrainGas {


  def trainGaB01: TrainGa[Data02] = new TrainGaAbstract {

    override def id: String = "trainGaB01"

    override protected def fitness: FitnessFunction[Data02] = FitnessFunctions.data02A01

    override def fullDescHeading: String = "B initial test"

    override protected def createNeuralNet: () => NeuralNet = () => NeuralNets.team02

    override protected def inMapper: InputMapperNn = new InputMapperNnTeam(1.0)

    override protected def outMapper: OutputMapperNn = OutputMappers.om02
  }

  def trainGaB02: TrainGa[Data02] = new TrainGaAbstract {

    override def id: String = "trainGaB02"

    override protected def fitness: FitnessFunction[Data02] = FitnessFunctions.data02A02

    override def fullDescHeading: String =
      """B first impoement
        |Tryout better Fitness function
      """.stripMargin

    override protected def createNeuralNet: () => NeuralNet = () => NeuralNets.team02

    override protected def inMapper: InputMapperNn = new InputMapperNnTeam(1.0)

    override protected def outMapper: OutputMapperNn = OutputMappers.om02
  }

}
