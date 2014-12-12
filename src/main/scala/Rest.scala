/**
 * Created by blingannagari on 12/12/14.
 */

import akka.actor.ActorSystem
import akka.http.marshalling._
import akka.http.model._
import akka.http.server.directives.MethodDirectives
import akka.http.server.{Directives, Route}
import akka.stream.FlowMaterializer

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait API

trait NewAPI extends API

trait GetSupported {
  def get(url: String, params: Map[String, String]): Future[String]
}

trait BaseSystem {
  implicit val system: ActorSystem
}

case class Person(name: String, age: Int)


trait RouteService extends MethodDirectives with BaseSystem {
  import system.dispatcher

  var routes: List[Route] = List.empty[Route]

  implicit val materializer = FlowMaterializer()
  import akka.http.server.Directives._

  implicit def test(httpEntity: HttpEntity)

  def actComplete[T](implicit marshaller: ToResponseMarshaller[T]): Try[T] => Route = {
    case Success(v) => complete(v)
    case Failure(e) => complete(e)
  }

  def pingRoute = path("ping") {
    get { complete("pong!") }
  }

  def pongRoute = path("pong") {
    get { complete("pong!?") }
  }

  def generateGet(uri: String, f: String => String) = path(uri)  {
    get {
      complete {
        f
        "ok"
      }
    }
  }

  def generatePost[T](uri: String, params: List[String], f: String => String) = path(uri) {
    post {
      entity(as[T]) {
        e => {
          f
          complete("ok")
        }
      }
    }
  }

  routes = routes :+ pingRoute :+ pongRoute// :+ generateGet("hello", "hello" => "hello")

}
