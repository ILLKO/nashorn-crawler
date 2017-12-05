package nashorn

object Main {

  def main(args: Array[String]): Unit = {
    runJs
  }

  def runJvm = {
    new HttpWrapper().send("GET", "https://opennet.ru/", Map.empty, None)
  }

  private def runJs = {
    val engine = NashornEngine.init()

    engine.evalString("print('Hello World!');")

    val value = engine.evalResource("/fetch-wiki.js").asInstanceOf[String]
    println("In Scala " + value)
  }
}
