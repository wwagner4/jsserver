package common

import java.io.File

import common.Viz.Diagram

/**
  * Created by wwagner4 on 04/03/2017.
  */
object Viz {

  case class XY(
                 x: Number,
                 y: Number
               )

  case class DataRow(
                      name: String,
                      data: List[XY] = List.empty
                    )

  case class Diagram(
                      id: String,
                      title: String,
                      xLabel: String,
                      yLabel: String,
                      dataRows: List[DataRow] = List.empty
                    )

  def createDiagram(dia: Diagram)(implicit creator: VizCreator): Unit = {
    creator.createDiagram(dia)
  }
}

trait VizCreator {

  def createDiagram(dia: Diagram): Unit

}

case class VizCreatorGnuplot(outDir: File) extends VizCreator {


  def createDiagram(dia: Diagram): Unit = {
    val script =
      """
set terminal pngcairo  transparent enhanced font "arial,10" fontscale 1.0 size 600, 400
set output 'a.png'
set key inside left top vertical Right noreverse enhanced autotitle box lt black linewidth 1.000 dashtype solid
set minussign
$Mydata << EOD
11 22
44 55
77 80
EOD
$Mydata1 << EOD
11 33
44 66
50 99
51 91
53 92
57 93
60 94
77 91
EOD
plot \
$Mydata using 1:2 title 'a dat' with lines, \
$Mydata1 using 1:2 title 'b  dat' with impulses
      """
    val id = dia.id
    val filename = s"diagram_$id.gnuplot"
    val f = new File(outDir, filename)
    Util.writeToFile(f, pw => pw.print(script))
    println(s"wrote diagram $id to $f")
  }


}
