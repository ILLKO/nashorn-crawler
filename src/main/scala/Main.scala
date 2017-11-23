object Main {

  def main(args: Array[String]): Unit = {
    val engine = NashornEngine.init()

    engine.evalString("print('Hello World!');")

    engine.evalResource("fetch-wiki.js")
  }
}
