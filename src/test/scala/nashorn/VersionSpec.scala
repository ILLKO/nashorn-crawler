package nashorn

import java.util
import javax.script.ScriptContext

import org.specs2.mutable.Specification

class VersionSpec extends Specification {

  val javaVersion = System.getProperty("java.version")

  val json =
    """{
       "code": 200,
        "results" : [{
        "id" : 123,
        "data": {
            "field": "value"
          }
        }]
      }
    """

  val script =
    s"""
      |(function func(context) {
      |      var res = JSON.parse(context.json);
      |      entity.body = res;
      |})(typeof global !== "undefined" && global || typeof self !== "undefined" && self || this);
    """.stripMargin

  "test1" should {
    "1" in {
      val ne = NashornEngine.init()
      val map = new util.HashMap[String, AnyRef]()

      ne.sc.setAttribute("json", json, ScriptContext.ENGINE_SCOPE)
      ne.sc.setAttribute("entity", map, ScriptContext.ENGINE_SCOPE)
      val result = ne.evalString(script)

      val entity = ne.sc.getAttribute("entity")
      entity

      ok
    }
  }


}
