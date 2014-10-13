package edu.luc.etl.cs313.scala.stopwatch
package model

import common.Constants._

/** Contains the components of the passive time model. */
object time {

  /** The passive data model of the stopwatch. It does not emit any events. */
  trait TimeModel {
    def resetRuntime(): Unit
    def incRuntime(): Unit
    def getRuntime(): Int
    def setLaptime(): Unit
    def getLaptime(): Int
    def setRuntime(value: Int)
    def setLaptime(value: Int)
  }

  /** An implementation of the stopwatch data model. */
  class DefaultTimeModel extends TimeModel {
    private var runningTime = 0
    private var lapTime = -1
    override def resetRuntime() = runningTime = 0
    override def incRuntime()   = runningTime = (runningTime + SEC_PER_TICK) % SEC_PER_HOUR
    override def getRuntime()   = runningTime
    override def setLaptime()   = lapTime = runningTime
    override def getLaptime()   = lapTime
    override def setRuntime(value: Int) = runningTime = value
    override def setLaptime(value: Int) = lapTime = value
  }
}
