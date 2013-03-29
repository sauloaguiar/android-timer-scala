package edu.luc.etl.cs313.android.scala.clickcounter
package model.mutable

/**
 * An mutable bounded counter abstraction.
 *
 * @author laufer
 */
trait BoundedCounter extends Counter {

  def min: Int

  def max: Int

  /**
   * Resets the counter value.
   */
  def reset(): Unit

  /**
   * Indicates whether the counter is full (at its maximum).
   */
  def isFull(): Boolean

  /**
   * Indicates whether the counter is empty (at its minimum).
   */
  def isEmpty(): Boolean
}