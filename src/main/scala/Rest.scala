/**
 * Created by blingannagari on 12/12/14.
 */

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.marshalling._
import akka.http.model._
import akka.http.server.directives.MethodDirectives
import akka.http.server.directives.ParameterDirectives.ParamDef
import akka.http.server.{Directive, Directives, Route}
import akka.http.unmarshalling.Unmarshaller
import akka.shapeless.HNil
import akka.stream.FlowMaterializer
import rx.core.{Rx, Var}
import akka.http.server._

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

trait BootedSystem {
  this: BaseSystem =>
  implicit val system = ActorSystem("TestSystem")
}

case class Person(name: String, age: Int)

case class Filter(firstName: String, lastName: Option[String])


trait RouteService extends MethodDirectives with BootedSystem with BaseSystem {
  import system.dispatcher

  val routes = Var(List.empty[Route])

  import akka.http.server.Directives._

  val echo1 = (str: String) =>  { str }

  def pingRoute = path("ping") {
    get { complete("pong!") }
  }

  def pongRoute = path("pong") {
    get { complete("pong!?") }
  }

  def generateGet(uri: String, f: String => String) = path(uri)  {
    get {
      complete {
        f("hello")
        //"ok"
      }
    }
  }

  def generateGetPathWithParams(uri: String, param1: String) = {
    val path1 = path(uri) & parameters('param1.as[Int])
  }

  def generatePost(uri: String) = ???

  def generateGetWithParams(uri: String, f: String => String) = path(uri) {
    get {
      parameters('firstName.as[String], 'lastName.as[String] ?).as(Filter) {
        filterParams: Filter => {
          complete("ok")
        }
      }
    }
  }

//  def addGet[T](uri: String, params: ParamDef, f: String => String): Unit = {
//    val route = generateGet(uri, f)
//    routes() = routes() :+ route
//  }

  routes() = routes() :+ pingRoute :+ pongRoute // :+ addGet(echo1)


//  def generatePost[T](uri: String, params: List[String], f: String => String) = path(uri) {
//    post {
//      entity(as[T]) {
//        e => {
//          f
//          complete("ok")
//        }
//      }
//    }
//  }

  //routes = routes :+ pingRoute :+ pongRoute// :+ generateGet("hello", "hello" => "hello")

  val concRoute = Rx {routes().reduceLeft(_ ~ _)}

}

//object SampleMethods extends RouteService{
//  import akka.http.server.Directives._
//  val echo = (str: String) =>  { str }
//  def testGet = {
//    val uri = "colors"
//    val params = parameters('red.as[Int], 'name.as[String].?)
////    addGet(uri, params, echo)
//
//    //val uriWithParams= parameters('name.as[String], 'last.as[String].?) {
//
//    }
//
//}

object TestRest extends App with BootedSystem with BaseSystem with RouteService {
  import system.dispatcher
  implicit val materializer = FlowMaterializer()

  val echo = (str: String) =>  { str }
  println("size of routes before: " + routes().size)
//  addGet("test", echo)
  println("size of routes after: " + routes().size)
  println("route list: " + routes)

  val binding = Http().bind(interface = "localhost", port = 1234)
  val materializedMap = binding startHandlingWith(concRoute())
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  def testGetWithParams = {
    //val uri = path("color")

  }

  Console.readLine()
  binding.unbind(materializedMap).onComplete(_ ⇒ system.shutdown())
}



