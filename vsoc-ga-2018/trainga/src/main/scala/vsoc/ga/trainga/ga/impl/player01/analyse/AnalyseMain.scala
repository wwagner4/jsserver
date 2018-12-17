package vsoc.ga.trainga.ga.impl.player01.analyse

import java.nio.file.{Files, Path}

import entelijan.viz.Viz
import entelijan.viz.VizCreator
import entelijan.viz.creators.VizCreatorGnuplot
import vsoc.ga.trainga.config.ConfigHelper

object AnalyseMain extends App {

  implicit val wd: Path = ConfigHelper.workDir
  val reader = new CsvReaderDataPlayer01()
  val datas = reader.read(s"trainGaPlayer01Simple")

  for (d <- datas) {
    println(d)
  }

  val kicks = Viz.DataRow(
    name = Some("kicks"),
    data = datas.map(d => Viz.XY(d.iterations, d.kicks)),
  )
  val goals = Viz.DataRow(
    name = Some("kicks"),
    data = datas.map(d => Viz.XY(d.iterations, d.goals)),
  )
  val dia = Viz.Diagram(
    id = s"player01",
    title = s"Player 01",
    dataRows = Seq(kicks, goals)
  )

  val scripDir = ConfigHelper.workDir.resolve(".script")
  if (!Files.exists(scripDir)) Files.createDirectories(scripDir)
  val diaDir = ConfigHelper.workDir.resolve("diagram")
  if (!Files.exists(diaDir)) Files.createDirectories(diaDir)
  implicit val creator: VizCreator[Viz.XY] =
    VizCreatorGnuplot(scripDir.toFile, diaDir.toFile, execute = true)
  Viz.createDiagram(dia)


}