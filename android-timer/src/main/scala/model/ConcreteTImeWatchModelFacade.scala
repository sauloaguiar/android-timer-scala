package edu.luc.etl.scala.timer
package model

import common.TimeWatchModel
import model.clock._
import model.state.{DefaultTimerWatchStateMachine,TimerWatchState}
import model.time.DefaultTimeModel
import model.time.TimeModel
import android.util.Log
import common.{TimeWatchModel,TimeWatchMemento}

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



  override def getMemento() = new TimeWatchMemento {
    override val runTime: Int = {
      Log.i("TimerWatchAndroid","Saving RUNTIME " + timeModel.getRuntime())
      timeModel.getRuntime
    }
    override val stateID: String = {
      Log.i("TimerWatchAndroid","Saving state " + stateMachine.getState().getStateName())
      stateMachine.getState().getStateName()
    }
  }

  override def restoreFromMemento(memento: TimeWatchMemento): Unit = {

    timeModel.setRuntime(memento.runTime)
    Log.i("TimerWatchAndroid","Loading RUNTIME " + memento.runTime)
    //stateMachine.actionStart()
    Log.i("TimerWatchAndroid","Loading STATEID " +memento.stateID.getStateName())
    stateMachine.resumeState(memento.stateID,memento.runTime)
    //stateMachine.actionUpdateView()
    //stateMachine.actionUpdateView()
  }
}
