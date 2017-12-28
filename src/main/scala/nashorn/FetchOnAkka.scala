package nashorn

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.{ Failure, Success }

object FetchOnAkka {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  def fetch(url: String, options: Map[String, AnyRef] = Map.empty) = {

    val method = options.getOrElse("method", "GET")
    val headers = options.getOrElse("headers", Map.empty)
    val body = options.get("body")

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = url,
      headers = convertHeaders(headers)
    ))

    responseFuture
      .onComplete {
        case Success(res) => println(res)
        case Failure(_)   => sys.error("something wrong")
      }
  }

  def convertHeaders() = {

  }

  def main(args: Array[String]): Unit = {
    fetch("http://akka.io")
  }
}
