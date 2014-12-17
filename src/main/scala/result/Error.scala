package result

/**
 * Created by blingannagari on 17/12/14.
 */

trait Error {
  def message: String
  def cause: Option[Error]
}

object Error {
  def apply(msg: String, cs: Option[Error] = None) = new Error {
    def message = msg
    def cause = cs
  }
}
