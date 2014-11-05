package edu.luc.etl.scala.timer
package model

/**
 * Created by sauloaguiar on 10/30/14.
 */
object time {

  // Passive data model for the timer
  trait TimeModel {
    def isFull(): Boolean
    def hasTimedOut(): Boolean
    def resetRuntime(): Unit
    def incRuntime(): Unit
    def decRuntime(): Unit
    def getRuntime(): Int
    def setRuntime(value: Int): Unit
  }

  // Default implementation for the timer
  class DefaultTimeModel extends TimeModel {

    private var runningTime = 0

    override def resetRuntime(): Unit = runningTime = 0
    override def incRuntime(): Unit = runningTime = runningTime + 1
    override def setRuntime(value: Int): Unit = runningTime = value
    override def getRuntime(): Int = runningTime
    override def decRuntime(): Unit =  runningTime = runningTime - 1

    override def hasTimedOut(): Boolean = runningTime == 0

    override def isFull(): Boolean = runningTime == 99
  }
}
