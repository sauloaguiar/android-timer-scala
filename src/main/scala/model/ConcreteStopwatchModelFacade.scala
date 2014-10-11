package edu.luc.etl.cs313.scala.stopwatch
package model

import common.StopwatchModel
import time._
import clock._
import state.{DefaultStopwatchStateMachine, StopwatchStateMachine}

/** A facade implementation of the model interface. */
trait ConcreteStopwatchModelFacade extends StopwatchModel {

  val timeModel: TimeModel   = new DefaultTimeModel
  val clockModel: ClockModel = new DefaultClockModel(stateMachine)
  val stateMachine: StopwatchStateMachine =
    new DefaultStopwatchStateMachine(timeModel, clockModel, listener)

  // methods in StopwatchUIListener
  override def onStartStop() = stateMachine.onStartStop()
  override def onLapReset()  = stateMachine.onLapReset()

  // methods in Startable
  override def onStart()     = stateMachine.actionInit()
  override def onStop()      = clockModel.stop()
}