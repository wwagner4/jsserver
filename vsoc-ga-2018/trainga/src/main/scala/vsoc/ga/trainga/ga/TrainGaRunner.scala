package vsoc.ga.trainga.ga

import java.nio.file.{Files, Path, Paths}
import java.util.{Comparator, Optional}

import org.slf4j.LoggerFactory
import vsoc.ga.common.config.{ConfigHelper, ConfigTrainGa}
import vsoc.ga.common.data.{CsvWriter, Data02}
import vsoc.ga.common.describe.DescribableFormatter
import vsoc.ga.common.persist.{Persistor, Persistors}
import vsoc.ga.common.{UtilPath, UtilReflection, UtilTransform}
import vsoc.ga.trainga.thinner.Thinner

object TrainGaRunner {

  private val log = LoggerFactory.getLogger(TrainGaRunner.getClass)

  private val workDir = UtilPath.workDir

  def run (cfg: ConfigTrainGa): Unit = {

    val id = cfg.id
    val nr = cfg.nr
    require(!id.isEmpty)
    require(!nr.isEmpty)
    def persistor: Persistor = Persistors.nio(workDir)
    val trainDir = Paths.get(id, nr)
    val persDir = persistor.dir(trainDir)

    val dataFile = persDir.resolve(s"$id-$nr-data.csv")

    val dh = new CsvWriter(dataFile)

    val fileNameOpt: Optional[Path] = Files.list(persDir)
      .filter(p => p.getFileName.toString.endsWith("ser"))
      .sorted(Comparator.reverseOrder())
      .findFirst()

    val tga: TrainGa[Data02] = UtilTransform.asOption(fileNameOpt)
      .map(p => p.getFileName.toString)
      .flatMap { file: String =>
        val path = trainDir.resolve(file)
        log.info(s"loading population from $path")
        persistor.load(path)(ois => new TrainGaPersist[Data02].load(ois))
      }
      .getOrElse {
        log.info(s"could not population from $trainDir. creating a new one")
        UtilReflection.call(TrainGas, cfg.id, classOf[TrainGa[Data02]])
      }

    val desc = DescribableFormatter.format(tga, 0)
    tga.listeners = tga.listeners :+ persListener :+ dataListener
    log.info(s"start ${tga.id}-${cfg.nr} at iteration ${tga.iterations.getOrElse(0)}\n\n--------------------------------------------------------\n$desc")
    tga.run(cfg.id, cfg.nr)

    def persListener: TrainGaListener[Data02] = (iteration: Int, _: Option[Data02]) => {
      val popnr = f"$iteration%04d"
      val filename = s"pop$popnr.ser"
      val filePath = trainDir.resolve(filename)
      log.info(s"saving population to $filePath")
      persistor.save(filePath)(oos => new TrainGaPersist[Data02].save(tga, oos))
      Thinner.thinFromTrainGaId(id, nr)
    }
    def dataListener: TrainGaListener[Data02] = (_: Int, sd: Option[Data02]) => {
      sd.foreach(_sd => dh.writeLine(_sd))
      log.info(s"wrote data to $dh")
    }

  }



}
