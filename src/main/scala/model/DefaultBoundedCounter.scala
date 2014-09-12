package edu.luc.etl.cs313.android.scala.clickcounter
package model

/**
 * A stateful implementation of a bounded counter.
 */
class DefaultBoundedCounter(_min: Int, _max: Int) extends BoundedCounter {

  // Reflection in conjunction with default argument value is very messy.
  // This explicit default constructor makes it very easy to create
  // new instances through reflection.
  def this() = this(0, 5)

  require { _min < _max }

  private var value = _min

  /**
   * Indicates whether this counter currently satisfies its internal data
   * invariant: the counter value is within the bounds.
   *
   * @return whether the data invariant is satisfied
   */
  protected def dataInvariant() = min <= value && value <= max

  override def get() = value

  override def min = _min

  override def max = _max

  override def increment() {
    assert { dataInvariant && ! isFull }
    value += 1
    assert { dataInvariant }
  }

  override def decrement()  {
    assert { dataInvariant && ! isEmpty }
    value -= 1
    assert { dataInvariant }
  }

  override def reset() {
    value = min
    assert { dataInvariant }
  }

  override def isFull() = value >= max

  override def isEmpty() = value <= min
}
