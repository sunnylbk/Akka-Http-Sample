package http

import akka.http.model.{HttpRequest, HttpResponse}
import logging.Logging
import result.{Failure, Success, Result}
import runtime._
import runtime.config.TypesafeConfig
import scala.concurrent.ExecutionContext

import akka.pattern.ask
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.AskTimeoutException

import akka.io.IO
import akka.http.Http
import akka.http.model._
import HttpMethods._

import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Sink
import akka.stream.FlowMaterializer

import scala.concurrent.{Future, ExecutionContext}
import scala.io.StdIn
import scala.concurrent.duration._


/**
 * Created by blingannagari on 17/12/14.
 */

//trait RunTime {
//  type Process[+A]
//  def run[A](process: Process[A])(implicit ec: ExecutionContext): Future[Result[A]]
//  def stop: Unit
//}
//
//trait Test

trait Server {

  val name: String

  private def systemName = s"$name-server-system"
  private lazy val akkaConfig = TypesafeConfig.akkaConfig(systemName)
  protected lazy implicit val system = ActorSystem.create(
    name = systemName,
    config = akkaConfig)

  implicit val executionContext: ExecutionContext

  def main(args: Array[String]) = {
    val interface = "localhost"
    val port = 1234
    val logger = org.slf4j.LoggerFactory.getLogger(name)
    implicit val askTimeout: Timeout = 500.millis

    import system.dispatcher
    implicit val materializer = FlowMaterializer()
//    val binding = IO(Http) ? Http.Bind(interface = interface, port = port)
//    binding.onSuccess {
//      case Http.ServerBinding(_, connectionStream) =>
//        connectionHandler(connectionStream)
//        logger.info(s"Starting server at $interface:$port")
//        println(s"PRESS ANY KEY to stop...")
//        StdIn.readLine
//        runtime.stop
//        system.shutdown
//    }
//    binding.onFailure {
//      case e: AskTimeoutException =>
//        logger.error(s"An error occured while starting server at $interface:$port: ${e.getMessage}")
//        runtime.stop
//        system.shutdown
//    }
  val binding = Http().bind(interface = "localhost", port = 1234)
    val materializedMap = binding startHandlingWithAsyncHandler(router)
  }


//  private def requestHandler =
//    router andThen runtime.run andThen (_ flatMap resultHandler) orElse notFoundRouter andThen (_ recoverWith exceptionHandler)

  val runtime: Runtime with Logging

  def router: HttpRequest =>  (Future[HttpResponse])

  private def resultHandler = successHandler orElse errorHandler


  private def successHandler: PartialFunction[Result[HttpResponse], Future[HttpResponse]] = {
    case Success(value) => Future.successful(value)
  }

  def errorHandler: PartialFunction[Result[HttpResponse], Future[HttpResponse]] = {
    case Failure(error) => Future.successful(HttpResponse(500))
  }

  protected def notFoundRouter: PartialFunction[HttpRequest, Future[HttpResponse]] = {
    case _ => Future.successful(HttpResponse(404))
  }

  private def exceptionHandler: PartialFunction[Throwable, Future[HttpResponse]] = {
    case e: Throwable => Future.successful(HttpResponse(500))
  }
}
