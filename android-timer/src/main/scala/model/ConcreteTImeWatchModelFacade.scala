package edu.luc.etl.scala.timer
package model

import common.TimeWatchModel
import model.clock._
import model.state.DefaultTimerWatchStateMachine
import model.time.DefaultTimeModel
import model.time.TimeModel

/**
 * Created by sauloaguiar on 11/3/14.
 */
trait ConcreteTimeWatchModelFacade extends TimeWatchModel {

  val timeModel: TimeModel = new DefaultTimeModel
  val clockModel: ClockModel = new DefaultClockModel(stateMachine)
  val stateMachine: DefaultTimerWatchStateMachine = new DefaultTimerWatchStateMachine(timeModel, clockModel, listener)

  // methods in UIHandling
  override def onButtonPress(): Unit = stateMachine.onButtonPress()

  // methods in Startable
  override def onStart(): Unit = stateMachine.actionInit()
  override def onStop(): Unit = clockModel.stop()
}
