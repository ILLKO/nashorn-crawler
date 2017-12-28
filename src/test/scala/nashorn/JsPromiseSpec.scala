package nashorn

import org.specs2.concurrent.ExecutionEnv
import org.specs2.execute.Result
import org.specs2.mutable.Specification

class JsPromiseSpec extends Specification {

  "promise" should {
    val dummy = "dummy"

    "then once success" >> { implicit ee: ExecutionEnv =>

      def code(resolve: String => Unit, reject: Throwable => Unit) = {
        resolve(dummy)
      }

      val p = new JsPromise[String](code)

      p.`then`(s => s === dummy).f.await
    }

    "then twice success" >> { implicit ee: ExecutionEnv =>

      def code(resolve: String => Unit, reject: Throwable => Unit) = {
        resolve(dummy)
      }

      val p = new JsPromise[String](code)

      p.`then`(s => s + s).`then`(s => s === dummy + dummy).f.await
    }


    "not change it's state after fulfilled" >> { implicit ee: ExecutionEnv =>

      def code(resolve: String => Unit, reject: Throwable => Unit) = {
        resolve(dummy)
        reject(new RuntimeException("ex"))
      }

      val p = new JsPromise[String](code)

      p.`then`(s => s === dummy, ex => throw new RuntimeException("should not be called")).f.await
    }

    "not change it's state after fulfilled delayed" >> { implicit ee: ExecutionEnv =>

      def code(resolve: String => Unit, reject: Throwable => Unit) = {
        Thread.sleep(100)
        resolve(dummy)
        reject(new RuntimeException("ex"))
      }

      val p = new JsPromise[String](code)

      p.`then`(s => s === dummy, ex => throw new RuntimeException("should not be called")).f.await
    }

    "not change it's state after fulfilled delayed 2" >> { implicit ee: ExecutionEnv =>

      def code(resolve: String => Unit, reject: Throwable => Unit) = {
        resolve(dummy)
        Thread.sleep(100)
        reject(new RuntimeException("ex"))
      }

      val p = new JsPromise[String](code)

      p.`then`(s => s === dummy, ex => throw new RuntimeException("should not be called")).f.await
    }

    "not change it's state after rejected" >> { implicit ee: ExecutionEnv =>

      def code(resolve: String => Unit, reject: Throwable => Unit) = {
        reject(new RuntimeException("ex"))
        resolve(dummy)
      }

      val p = new JsPromise[String](code)

      p.`then`(s => failure("should not be called").asInstanceOf[Result])
        .`catch`(ex => (ex === new RuntimeException("ex")).toResult)
        .f.await
    }
  }

}
