package nashorn.js

import akka.dispatch.Futures
import nashorn.js

import scala.concurrent.{Future, Promise}

class JsPromiseNew[+A](
                   executor: Function2[Function1[A | Thenable[A], _], Function1[scala.Any, _], _])
  extends Object with Thenable[A] {

//  val p: Promise[A] = Futures.promise[A]()
//  val f: Future[A] = p.future
//
//  code(resolve, reject)

  def `then`[B](
                 onFulfilled: Function1[A, B | Thenable[B]],
                 onRejected: UndefOr[Function1[scala.Any, B | Thenable[B]]] = undefined): Thenable[B] = ???

  def `then`[B >: A](
                      onFulfilled: Unit,
                      onRejected: js.UndefOr[Function1[scala.Any, B | Thenable[B]]]): Thenable[B] = ???

  def `catch`[B >: A](
                       onRejected: js.UndefOr[Function1[scala.Any, B | Thenable[B]]] = undefined): JsPromiseNew[B] = ???
}