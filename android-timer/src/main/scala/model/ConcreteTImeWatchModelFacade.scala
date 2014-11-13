package edu.luc.etl.scala.timer
package model

import edu.luc.etl.scala.timer.common.TimeWatchModel
import edu.luc.etl.scala.timer.model.clock._
import edu.luc.etl.scala.timer.model.state.DefaultTimerWatchStateMachine
import edu.luc.etl.scala.timer.model.time.{DefaultTimeModel, TimeModel}

/**
 * Created by sauloaguiar on 11/3/14.
 */
trait ConcreteTimeWatchModelFacade extends TimeWatchModel {

  val timeModel: TimeModel = new DefaultTimeModel
  val clockModel: ClockModel = new DefaultClockModel(stateMachine)
  val stateMachine: DefaultTimerWatchStateMachine = new DefaultTimerWatchStateMachine(timeModel, clockModel, listener)

  // methods in UIHandling
  override def onButtonPress(): Unit = stateMachine.onButtonPress()
  override def onTimerChanged(value: Int) = stateMachine.onTimerChanged(value)

  // methods in Startable
  override def onStart(): Unit = stateMachine.actionInit()
  override def onStop(): Unit = clockModel.stop()

}
