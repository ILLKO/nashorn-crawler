package nashorn

import java.util.concurrent.Executors
import java.util.function.Consumer
import javax.script.{ScriptContext, ScriptEngine, SimpleScriptContext}
import org.slf4j.{Logger, LoggerFactory}
import scala.io.Source

class NashornEngine(engine: ScriptEngine, sc: ScriptContext) {

  def evalResource(resource: String): AnyRef = {
    print(s"Running $resource ")
    val code = NashornEngine.readResource(resource)
    val result = engine.eval(code, sc)
    println(s" done")
    result
  }

  def evalString(script: String): AnyRef = {
    engine.eval(script, sc)
  }
}

object JavascriptLogger {
  val logger: Logger = LoggerFactory.getLogger(JavascriptLogger.getClass)
}

object NashornEngine {

  val globalScheduledThreadPool = Executors.newScheduledThreadPool(20)

  def init(): NashornEngine = {

    import javax.script.ScriptEngineManager

    val manager = new ScriptEngineManager
    val engine: ScriptEngine = manager.getEngineByName("nashorn")

    val sc: SimpleScriptContext = initScriptContext(engine)

    val ne = new NashornEngine(engine, sc)

    initPolyFills(ne)

    ne
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


  def initScriptContext(engine: ScriptEngine): SimpleScriptContext = {

    val sc = new SimpleScriptContext()

    sc.setBindings(engine.createBindings, ScriptContext.ENGINE_SCOPE)
    sc.setAttribute("consoleLogInfo", consoleLogInfo, ScriptContext.ENGINE_SCOPE)
    sc.setAttribute("consoleLogError", consoleLogError, ScriptContext.ENGINE_SCOPE)
    sc.setAttribute("__NASHORN_POLYFILL_TIMER__", globalScheduledThreadPool, ScriptContext.ENGINE_SCOPE)

    //    val initialBindings = sc.getBindings(ScriptContext.ENGINE_SCOPE)

    sc
  }

  def initPolyFills(engine: NashornEngine): Unit = {
//    engine.evalResource("/nashorn-polyfill.js")
    engine.evalResource("/blob-polyfill.js")
    engine.evalResource("/global-polyfill.js")
    engine.evalResource("/timer-polyfill.js")
    engine.evalResource("/xml-http-request-polyfill.js")
    engine.evalResource("/es6-promise-polyfill.js")
//    engine.evalResource("/promise.js")
    engine.evalResource("/fetch.js")
  }

  def readResource(resource: String): String = {
    val is = getClass.getResourceAsStream(resource)
    Source.fromInputStream(is).mkString
  }

  //  val js = "print('Hello World!');"
  //  engine.eval(js)

}