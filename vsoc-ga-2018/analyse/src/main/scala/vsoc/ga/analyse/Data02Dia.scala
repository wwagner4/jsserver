package vsoc.ga.analyse

import vsoc.ga.common.data.Data02

class Data02Dia extends DataDia[Data02](new CsvReaderData02) {

  override def origin(id: String, nr: String): Data02 = Data02(id, nr)

  override def x(data: Data02): Double = data.iterations

  override def y(data: Data02): Double = data.score
}

class CsvReaderData02 extends CsvReader[Data02] {
  override def toBean(line: String): Data02 = {
    val a = line.split(";")
    Data02(
      a(0),
      a(1),
      toInt(a(2)),
      toDouble(a(3)),
      toDouble(a(4)),
      toDouble(a(5)),
      toDouble(a(6)),
      toDouble(a(7)),
      toDouble(a(8)),
      toDouble(a(9)),
      toDouble(a(10)),
      toDouble(a(11)),
      toDouble(a(12)),
      toDouble(a(13)),
      toDouble(a(14)),
      toDouble(a(15)),
      toDouble(a(16))
    )

  }
}
