package vsoc.ga.trainga.ga

import java.util.concurrent.Executors

import vsoc.ga.common.commandline.WithConfigRunner
import vsoc.ga.common.config.Config

object TrainGaMain extends App with WithConfigRunner {

  runWithConfig(args, trainGa, TrainGaMain.getClass.getSimpleName)

  def trainGa(cfg: Config): Unit = {
    val ec = Executors.newFixedThreadPool(cfg.trainings.size)
    val wdBase = cfg.workDirBase
    for (c <- cfg.trainings) {
      ec.execute(() => TrainGaRunner.run(wdBase, c))
    }
  }


}
