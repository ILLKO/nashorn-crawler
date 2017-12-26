package nashorn

import akka.dispatch.Futures

import scala.concurrent.{Future, Promise}
import JsPromise1._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class JsPromise1[T](code: JsPromiseFunc[T]) {

  val p: Promise[T] = Futures.promise[T]()
  val f: Future[T] = p.future

  code(resolve, reject)

  def resolve(t: T): Unit = {
    p.trySuccess(t)
  }

  def reject(ex: Throwable): Unit = {
    p.tryFailure(ex)
  }

  def `then`[S](onFulfilled: T => S, onRejected: Throwable => Throwable): JsPromise1[S] = {
    new JsPromise1((resolve, reject) => f.onComplete {
      case Success(value) => resolve(onFulfilled(value))
      case Failure(ex) => reject(onRejected(ex))
    })
  }

  def `then`[S](onFulfilled: T => S): JsPromise1[S] = {
    new JsPromise1((resolve, reject) => f.onSuccess {
      case value => resolve(onFulfilled(value))
    })
  }

  def `catch`(onRejected: Throwable => T): JsPromise1[T] = {
    new JsPromise1((resolve, reject) => f.onComplete {
      case Success(value) => Success(value)
      case Failure(ex) => Success(onRejected(ex))
    })
  }

}

object JsPromise1 {

  type JsPromiseFunc[T] = (T => Unit, Throwable => Unit) => Unit

}