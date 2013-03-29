package edu.luc.etl.cs313.android.scala.clickcounter
package model.mutable

/**
 * An mutable counter abstraction.
 *
 * @author laufer
 */
trait Counter {

  /**
   * Returns the counter value.
   */
  def get(): Int

  /**
   * Increments the counter value.
   */
  def increment(): Unit

  /**
   * Decrements the counter value.
   */
  def decrement(): Unit
}