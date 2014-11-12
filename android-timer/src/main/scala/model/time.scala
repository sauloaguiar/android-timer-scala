package edu.luc.etl.scala.timer
package model

import common.Constants._

/**
 * Created by sauloaguiar on 10/30/14.
 */
object time {

  // Passive data model for the timer
  trait TimeModel {
    def hasReachedMax(): Boolean
    def isResume(): Boolean
    def setResumeFlag(flag: Boolean): Unit
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
    private var isResumeFlag = false
    private val MAX = 99

    override def resetRuntime(): Unit = runningTime = 0
    override def incRuntime(): Unit = runningTime = runningTime + SEC_PER_TICK
    override def setRuntime(value: Int): Unit = runningTime = value
    override def getRuntime(): Int = runningTime
    override def decRuntime(): Unit =  runningTime = runningTime - SEC_PER_TICK
    override def isResume(): Boolean = isResumeFlag
    override def setResumeFlag(flag: Boolean): Unit = isResumeFlag = flag
    override def hasTimedOut(): Boolean = runningTime == 0
    override def hasReachedMax(): Boolean = runningTime == MAX
  }
}
