package edu.luc.etl.cs313.scala.stopwatch
package model

import common.listeners.StopwatchUIUpdateListener
import time._
import clock._
import state.{DefaultStopwatchStateMachine, StopwatchStateMachine}

/** An implementation of the model facade. */
class ConcreteStopwatchModelFacade(listener: StopwatchUIUpdateListener) extends StopwatchModelFacade {

  val timeModel: TimeModel   = new DefaultTimeModel
  val clockModel: ClockModel = new DefaultClockModel
  val stateMachine: StopwatchStateMachine =
    new DefaultStopwatchStateMachine(timeModel, clockModel, listener)

  clockModel.setOnTickListener(stateMachine)

  override def onStart()     = stateMachine.actionInit()
  override def onStartStop() = stateMachine.onStartStop()
  override def onLapReset()  = stateMachine.onLapReset()
}