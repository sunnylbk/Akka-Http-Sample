package runtime

import logging.DefaultLogging

/**
 * Created by blingannagari on 17/12/14.
 */
class ExampleRuntime[Env <: DefaultEnvironment](val name: String, val environment: Env) extends DefaultRuntime[Env]
with DefaultLogging {

  def stop = {

  }

}
