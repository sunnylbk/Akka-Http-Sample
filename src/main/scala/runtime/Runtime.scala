package runtime

/**
 * Created by blingannagari on 17/12/14.
 */

import result.Result

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait Runtime {
  type Process[+A]
  def run[A](process: Process[A])(implicit ec: ExecutionContext): Future[Result[A]]
  def stop: Unit
}