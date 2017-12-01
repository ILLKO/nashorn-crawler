package nashorn

object Main {

  def main(args: Array[String]): Unit = {
    runJvm
  }

  def runJvm = {
    new HttpWrapper().send("GET", "https://opennet.ru/", Map.empty, None)
  }

  private def runJs = {
    val engine = NashornEngine.init()

    engine.evalString("print('Hello World!');")

    engine.evalResource("/fetch-wiki.js")
  }
}
