package vsoc.ga.trainga.ga.impl.player01

import vsoc.ga.genetic.FitnessFunction

class FitnessFunctionPlayer01 extends FitnessFunction[DataPlayer01] {
  override def fitness(score: DataPlayer01): Double = {
    val k: Double = score.kicks
    val g: Double = score.goals
    k + (g * 2.0)
  }
}
