package test

import akka.http.model.{HttpRequest, HttpResponse}
import http.TreeRouter

import scala.util.matching.Regex

/**
 * Created by blingannagari on 17/12/14.
 */
//object TreeRouterTest extends App {
//
//  def test = {
//    val router = new TreeRouter(
//      "GET /" -> index _
//    ).run
//
//    println("router: " + router)
//  }
//
//  /**
//   * This controller method logs an info message and returns an http response.
//   */
//  def index(params: Regex)(request: HttpRequest): Process[HttpResponse] = for {
//    _ <- println("index page accessed")
//  } yield HttpResponse(entity = "index")
//}
