package result

/**
 * Created by blingannagari on 17/12/14.
 */

trait Monoid[A] {
  def zero: A
  def append(a1: A, a2: => A): A
}