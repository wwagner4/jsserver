package vsoc.ga.trainga.nn

import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, NeuralNetConfiguration}
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.lossfunctions.LossFunctions
import vsoc.ga.common.describe.{DescribableFormatter, PropertiesProvider}
import vsoc.ga.trainga.nn.impl.NnWrapperAbstract

object NeuralNets {

  def default: NeuralNet = new NnWrapperAbstract {

    // id must be the name of the method creating the neural net
    def id = "default"

    val numInputNodes = 2
    val numHiddenNodes = 5
    val numOutputNodes = 3

    protected def nnConfiguration(): MultiLayerConfiguration = {
      new NeuralNetConfiguration.Builder()
        .iterations(1)
        .weightInit(WeightInit.XAVIER)
        .list
        .layer(0, new DenseLayer.Builder()
          .nIn(numInputNodes)
          .nOut(numHiddenNodes)
          .activation(Activation.TANH)
          .build)
        .layer(1, new DenseLayer.Builder()
          .nIn(numHiddenNodes)
          .nOut(numHiddenNodes)
          .activation(Activation.TANH)
          .build)
        .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
          .activation(Activation.IDENTITY).nIn(numHiddenNodes)
          .nOut(numOutputNodes)
          .build)
        .build
    }

    override def fullDesc: String = "Default Neural Net. Used for testing purposes"
  }

  /**
    * Used in testcases should not be changed
    */
  def test: NeuralNet = new NnWrapperAbstract {

    // id must be the name of the method creating the neural net
    def id = "test"

    override val numInputNodes: Int = 2

    private val numHiddenNodes: Int = 5

    override val numOutputNodes: Int = 3

    override protected def nnConfiguration(): MultiLayerConfiguration = {
      new NeuralNetConfiguration.Builder()
        .iterations(1)
        .weightInit(WeightInit.XAVIER)
        .list
        .layer(0, new DenseLayer.Builder()
          .nIn(numInputNodes)
          .nOut(numHiddenNodes)
          .activation(Activation.TANH)
          .build)
        .layer(1, new DenseLayer.Builder()
          .nIn(numHiddenNodes)
          .nOut(numHiddenNodes)
          .activation(Activation.TANH)
          .build)
        .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
          .activation(Activation.IDENTITY).nIn(numHiddenNodes)
          .nOut(numOutputNodes)
          .build)
        .build
    }

    override def fullDesc: String = "Standard Neural Net. Used for testing purposes"


  }

  def team01: NeuralNet = new NnWrapperAbstract with PropertiesProvider {

    // id must be the name of the method creating the neural net
    def id = "team01"

    override def fullDesc: String = {
      val props = DescribableFormatter.format(properties, 0)
      s"""Feedforward NN with one hidden layers
         |140 (in) 100 (1) 100 (out) 4
         |$props
         |""".stripMargin

    }

    override def properties: Seq[(String, Any)] = Seq(
      ("id", id),
      ("num in", numInputNodes),
      ("num hidden", numHiddenNodes),
      ("num out", numOutputNodes),
      ("activation", "TANH"),
    )

    override val numInputNodes: Int = 140

    private val numHiddenNodes: Int = 150

    override val numOutputNodes: Int = 4

    override protected def nnConfiguration(): MultiLayerConfiguration = {
      new NeuralNetConfiguration.Builder()
        .iterations(1)
        .weightInit(WeightInit.XAVIER)
        .list
        .layer(0, new DenseLayer.Builder()
          .nIn(numInputNodes)
          .nOut(numHiddenNodes)
          .activation(Activation.TANH)
          .build)
        .layer(1, new DenseLayer.Builder()
          .nIn(numHiddenNodes)
          .nOut(numHiddenNodes)
          .activation(Activation.TANH)
          .build)
        .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
          .activation(Activation.IDENTITY).nIn(numHiddenNodes)
          .nOut(numOutputNodes)
          .build)
        .build
    }

  }

  def team02: NeuralNet = new NnWrapperAbstract with PropertiesProvider {

    // id must be the name of the method creating the neural net
    def id = "team02"

    override def fullDesc: String = {
      val props = DescribableFormatter.format(properties, 0)
      s"""Feedforward NN with two hidden layers
         |140 (in) 100 (1) 50 (2) 10 (out) 4
         |$props
         |""".stripMargin

    }

    override def properties: Seq[(String, Any)] = Seq(
      ("id", id),
      ("num in", numInputNodes),
      ("num hidden1", numHiddenNodes1),
      ("num hidden2", numHiddenNodes2),
      ("num hidden3", numHiddenNodes3),
      ("num out", numOutputNodes),
      ("activation", "TANH"),
    )

    override val numInputNodes: Int = 140

    private val numHiddenNodes1: Int = 100

    private val numHiddenNodes2: Int = 50

    private val numHiddenNodes3: Int = 10

    override val numOutputNodes: Int = 4


    override protected def nnConfiguration(): MultiLayerConfiguration = {
      new NeuralNetConfiguration.Builder()
        .list
        .layer(0, new DenseLayer.Builder()
          .nIn(numInputNodes)
          .nOut(numHiddenNodes1)
          .activation(Activation.TANH)
          .build)
        .layer(1, new DenseLayer.Builder()
          .nIn(numHiddenNodes1)
          .nOut(numHiddenNodes2)
          .activation(Activation.TANH)
          .build)
        .layer(2, new DenseLayer.Builder()
          .nIn(numHiddenNodes2)
          .nOut(numHiddenNodes3)
          .activation(Activation.TANH)
          .build)
        .layer(3, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
          .activation(Activation.IDENTITY).nIn(numHiddenNodes3)
          .nOut(numOutputNodes)
          .build)
        .build
    }

  }

}
