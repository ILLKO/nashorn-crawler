package nashorn

import java.security.cert.X509Certificate
import java.util

import org.apache.http.client.methods.RequestBuilder
import org.apache.http.concurrent.FutureCallback
import org.apache.http.impl.nio.client.{CloseableHttpAsyncClient, HttpAsyncClientBuilder}
import org.apache.http.message.BasicHeader
import java.util.ArrayList
import java.util.concurrent.Future

import org.apache.http.HttpResponse
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.entity.StringEntity
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.{SSLContextBuilder, TrustStrategy}

import scala.collection.JavaConverters._

class HttpWrapper {

  def open(method: String, url: String, headers: Map[String, String]) = {
    val requestBuilder = RequestBuilder.create(method)
    requestBuilder.setUri(url)

    //    headers.foreach {      case (name, value) =>
    //        requestBuilder.addHeader(name, value)
    //    }
  }

  def send(method: String, url: String, headers: Map[String, String], data: Option[String]): Future[HttpResponse] = {
    val requestBuilder = RequestBuilder.create(method)
    requestBuilder.setUri(url)

//    val client = HttpClients.custom()
//      .setSSLContext(sslContext)
//      .setSSLHostnameVerifier(new NoopHostnameVerifier())
//      .build()

    val clientBuilder = HttpAsyncClientBuilder.create()
    val httpHeaders = new util.ArrayList(headers.size)

    val headersList = headers.map { case (name, value) => new BasicHeader(name, value) }.asJavaCollection
    clientBuilder.setDefaultHeaders(headersList)

    requestBuilder.setEntity(data.map(d => new StringEntity(d)).orNull)

    val sslContext = new SSLContextBuilder().loadTrustMaterial(null, TrustAll).build()
    clientBuilder.setSSLContext(sslContext)
    clientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier())

    val httpclient = clientBuilder.build()
    httpclient.start()

    val callback = new WrapperCallBack(httpclient)

    httpclient.execute(requestBuilder.build(), null, callback)
  }

}

object TrustAll extends TrustStrategy {
  override def isTrusted(chain: Array[X509Certificate], authType: String): Boolean = {
    true
  }
}


class WrapperCallBack(httpClient: CloseableHttpAsyncClient) extends FutureCallback[HttpResponse] {
  override def completed(response: HttpResponse): Unit = {
    var body = org.apache.http.util.EntityUtils.toString(response.getEntity, "UTF-8")

    val statusLine = response.getStatusLine
    val status = statusLine.getStatusCode
    val statusText = statusLine.getReasonPhrase

    println(s"completed: $status $statusText")
    println("body: " + body)

    httpClient.close()
  }

  override def cancelled() = httpClient.close()

  override def failed(ex: Exception) = {
    val statusText = ex.getMessage

    println("failed: " + statusText)
    httpClient.close()
  }
}
