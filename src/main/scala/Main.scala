import java.util.concurrent.Executors
import java.util.function.Consumer
import javax.script.{ScriptContext, ScriptEngine, SimpleScriptContext}
import javax.script._

import org.slf4j.{Logger, LoggerFactory}

import scala.io.Source

object JavascriptLogger {
  val logger: Logger = LoggerFactory.getLogger(JavascriptLogger.getClass)
}

object Main {

  def readResource(resource: String): String = {
    val is = getClass.getResourceAsStream(resource)
    Source.fromInputStream(is).mkString
  }

  def runResource(resource: String)(implicit engine: ScriptEngine, sc: SimpleScriptContext): AnyRef = {
    print(s"Running $resource ")
    val code = readResource(resource)
    val result = engine.eval(code, sc)
    println(s" done")
    result
  }

  val consoleLogInfo: Consumer[Object] = new Consumer[Object] {
    override def accept(t: Object): Unit = {
      println(t)
      JavascriptLogger.logger.info("{}", t)
    }
  }

  val consoleLogError: Consumer[Object] = new Consumer[Object] {
    override def accept(t: Object): Unit = {
      println(t)
      JavascriptLogger.logger.error("{}", t)
    }
  }

  def initEngine(engine: ScriptEngine): SimpleScriptContext = {
    val globalScheduledThreadPool = Executors.newScheduledThreadPool(20)

    val sc = new SimpleScriptContext()

    sc.setBindings(engine.createBindings, ScriptContext.ENGINE_SCOPE)
    sc.setAttribute("consoleLogInfo", consoleLogInfo, ScriptContext.ENGINE_SCOPE)
    sc.setAttribute("consoleLogError", consoleLogError, ScriptContext.ENGINE_SCOPE)
    sc.setAttribute("__NASHORN_POLYFILL_TIMER__", globalScheduledThreadPool, ScriptContext.ENGINE_SCOPE)

    val initialBindings = sc.getBindings(ScriptContext.ENGINE_SCOPE)

    sc
  }

  def main(args: Array[String]): Unit = {
    import javax.script.ScriptEngineManager
    val manager = new ScriptEngineManager
    implicit val engine: ScriptEngine = manager.getEngineByName("nashorn")

    implicit val sc: SimpleScriptContext = initEngine(engine)

    val js = "print('Hello World!');"
    engine.eval(js)

//    runResource("nashorn-polyfill.js")
    runResource("blob-polyfill.js")
    runResource("global-polyfill.js")
    runResource("timer-polyfill.js")
    runResource("xml-http-request-polyfill.js")
    runResource("es6-promise-polyfill.js")
    runResource("fetch.js")
    runResource("fetch-wiki.js")
  }
}
