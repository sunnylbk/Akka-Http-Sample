package logging

import runtime.DefaultRuntime

/**
 * Created by blingannagari on 17/12/14.
 */
trait DefaultLogging extends Logging {
  self: DefaultRuntime[_] =>

  lazy val logger = new Slf4jLogger(name)
}
