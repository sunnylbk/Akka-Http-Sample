package example

import akka.http.model.{HttpRequest, HttpResponse}
import http.{Server, TreeRouter}
import runtime.{DefaultEnvironment, ExampleRuntime}

import scala.concurrent.{Future, ExecutionContext}
import scala.util.matching.Regex

/**
 * Created by blingannagari on 17/12/14.
 */
object example2 extends Server {

  val name = "example2"

  implicit val executionContext = ExecutionContext.Implicits.global

  val runtime = new ExampleRuntime(name, new DefaultEnvironment)

  val router = new TreeRouter(
    "GET /" -> index _
  ).run

  /**
   * This controller method logs an info message and returns an http response.
   */
  def index(params: Regex)(request: HttpRequest): Future[HttpResponse] = for {
    _ <- Future("index page accessed")
  } yield HttpResponse(entity = "index")
}
