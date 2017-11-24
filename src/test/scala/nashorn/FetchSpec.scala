package nashorn

import javax.script.Bindings

import org.specs2.mutable.Specification

class FetchSpec extends Specification {

  "js" should {
    "return value to java" in {
      val ne = NashornEngine.init()
      val obj = ne.evalString("var obj = { value: 1 };  obj; ").asInstanceOf[Bindings]
      val value = obj.get("value").asInstanceOf[Integer]
      value === 1
    }

    "return async" in {
      val s = """function plus(a, b, done) {

        setTimeout(function() {
          done(null, a + b);
        }, 100);
      }

      plus(1, 2, async());"""
    }
  }
}
