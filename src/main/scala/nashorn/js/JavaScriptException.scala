package nashorn.js

case class JavaScriptException(exception: scala.Any) extends RuntimeException {
  override def getMessage(): String = exception.toString()

//  override def fillInStackTrace(): Throwable = {
//    scala.scalajs.runtime.StackTrace.captureState(this, exception)
//    this
//  }
}