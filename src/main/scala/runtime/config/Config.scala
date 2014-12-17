package runtime.config

/**
 * Created by blingannagari on 17/12/14.
 */
trait Config {
  def get(path: String): Option[String]
}
