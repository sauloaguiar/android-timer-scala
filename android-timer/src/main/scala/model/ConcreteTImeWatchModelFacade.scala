package edu.luc.etl.scala.timer
package model

import android.util.Log
import edu.luc.etl.scala.timer.common.{TimeWatchMemento, TimeWatchModel}
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

  // methods in Startable
  override def onStart(): Unit = stateMachine.actionInit()
  override def onStop(): Unit = clockModel.stop()


  override def getMemento() = new TimeWatchMemento {
    override val runTime: Int = timeModel.getRuntime()
    override val stateID: Int = stateMachine.getCurrentState()
    override val resumeFlag : Boolean ={
      val result = stateMachine.getCurrentState() match {
        case R.string.STOPPED => false
        case R.string.BEEPING => false
        case R.string.RUNNING => true
        case _ => false
      }
      Log.i("TimerWatchAndroid","GET MEMENTO RESUME FLAG "+result)
      result
      }
  }

  override def restoreFromMemento(memento: TimeWatchMemento): Unit = {
    timeModel.setRuntime(memento.runTime)
    timeModel.setResumeFlag(memento.resumeFlag)
    memento.stateID match {
      case R.string.STOPPED => stateMachine.toStoppedState()
      case R.string.BEEPING => stateMachine.toBeepingState()
      case R.string.RUNNING => stateMachine.toRunningState()
    }

  }
}
