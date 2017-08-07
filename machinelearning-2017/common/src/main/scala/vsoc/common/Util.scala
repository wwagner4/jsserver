package vsoc.common

import java.io.{Closeable, File, PrintWriter}
import java.util.UUID

import scala.io.Source

/**
  * Util functions
  */
object Util {

  val workDirName = "vsoc"

  def writeToFile(file: File, op: (PrintWriter) => Unit): Unit = {
    val pw = new PrintWriter(file)
    try {op(pw)} finally {pw.close()}
  }

  def lines(f: File): Int = Source.fromFile(f).getLines.size

  def dirSub(parent: File, subDirName: String): File = {
    val dir = new File(parent, subDirName)
    if (!dir.exists()) dir.mkdirs()
    dir
  }

  def dirHome: File = {
    new File(System.getProperty("user.home"))
  }

  def dirWork: File = {
    dirSub(dirHome, "vsoc")
  }

  def dataDir: File = {
    dirSub(dirWork, "data")
  }

  def scriptsDir: File = {
    dirSub(dirWork, "scripts")
  }

  def imgDir: File = {
    dirSub(dirWork, "img")
  }

  def dataFile(fileName: String): File ={
    new File(dataDir, fileName)
  }

  def createTempDirectory: File = {
    val tmpDirName = System.getProperty("java.io.tmpdir")
    val uuid = UUID.randomUUID
    new File(new File(tmpDirName), "ml" + uuid.getMostSignificantBits)
  }

  def use[T <: Closeable](closeable: T)(f: T => Unit): Unit = {
    try {
      f(closeable)
    }
    finally {
      closeable.close()
    }
  }



}