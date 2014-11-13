package edu.luc.etl.scala.timer
package model

import android.util.Log
import edu.luc.etl.scala.timer.common.{TimerUIUpdateListener, UIHandling}
import edu.luc.etl.scala.timer.model.clock._
import edu.luc.etl.scala.timer.model.time.TimeModel

/**
 * Created by sauloaguiar on 10/30/14.
 */
object state {

  trait Initializable { def actionInit(): Unit }

  trait TimerStateMachine extends UIHandling with OnTickListener with Initializable {
    def getState(): TimerWatchState
    def actionUpdateView(): Unit
    def getCurrentState(): Int
  }

  /** A state in a state machine. This interface is part of the State pattern. */
  trait TimerWatchState extends UIHandling with OnTickListener with OnTimeoutListener {
    def onEntry(): Unit
    def onExit(): Unit
    def updateView(): Unit
   // def restartTimeout: Unit
    def getStateName(): Int
    def getStateButtonAction(): Int
    def onTimeout(): Unit = throw new UnsupportedOperationException("onTimeout")
    def onTick(): Unit = throw new UnsupportedOperationException("onTick")
    def onTimerChanged(value: Int): Unit = throw new UnsupportedOperationException("onTimerValueChosen")
  }

  class DefaultTimerWatchStateMachine(
    timeModel: TimeModel,
    clockModel: ClockModel,
    uiListener: TimerUIUpdateListener) extends TimerStateMachine {

    def getCurrentState(): Int = state.getStateName()
    private var state: TimerWatchState = _
    private var timeoutClock: TimeoutModel = _
    private var resumeStateFlag: Boolean = false
    protected def setState(newState: TimerWatchState): Unit = {
      if (state != null) state.onExit()
      state = newState
      state.onEntry()
      uiListener.updateState(state.getStateName(), state.getStateButtonAction())
    }
    def setResumeStateFlag(newStateFlag: Boolean){ resumeStateFlag = newStateFlag}

    /*def resumeState(newState: TimerWatchState, runtime: Int): Unit = {
      if (state != null) state.onExit()
      // actionInit()
      timeModel.setRuntime(runtime)
      state = newState
      //Log.i("TimerWatchAndroid","RESUME STATE UPDATE VIEW " + timeModel.getRuntime())
      actionUpdateView() //update time state.updateView
      //
      //actionStart()
      state.onEntry()

      uiListener.updateState(state.getStateName(), state.getStateButtonAction())

    }*/

    override def getState(): TimerWatchState = state


    // UI Based Events
    override def onButtonPress(): Unit = state.onButtonPress()
    override def onTick(): Unit = state.onTick()
    override def onTimerChanged(value: Int) = { timeModel.setRuntime(value); toStoppedCountingState() }

    // transitions
    def toStoppedState(): Unit = setState(STOPPED)
    def toRunningState(): Unit = setState(RUNNING)
    def toBeepingState(): Unit = setState(BEEPING)
    def toStoppedCountingState(): Unit = setState(STOPPED_COUNTING)

    //
    def updateUIRuntime(): Unit = uiListener.updateTime(timeModel.getRuntime)
    def enableButton(): Unit = uiListener.enableButton(true)
    def disableButton(): Unit = uiListener.enableButton(false)

    // actions
    override def actionInit(): Unit = { toStoppedState(); actionReset() }
    override def actionUpdateView(): Unit = state.updateView()
    def actionReset(): Unit = { timeModel.resetRuntime(); actionUpdateView() }
    def actionStart(): Unit = { clockModel.start() }
    def actionStop(): Unit = { clockModel.stop() }
    def actionInc(): Unit = {
      timeModel.incRuntime()
      if (timeModel.hasReachedMax()){
        toRunningState()
      } else {
        actionUpdateView()
      }
    }
    def actionDec(): Unit = { timeModel.decRuntime(); actionUpdateView() }
    def actionRestartTimeout(listener: OnTimeoutListener): Unit = {
      Log.i(TAG, "Start timeOut")
      if (timeoutClock == null) {
        timeoutClock = new DefaultTimeoutModel(listener)
      }
      timeoutClock.restartTimeout(3)
    }
    private val TAG = "TimerWatchAndroid"
    //def onResumeStoppedState(stoppedState: TimerWatchState): Unit = { actionRestartTimeout(state.this) }
    def actionStopTimeout(): Unit = {

      if (timeoutClock != null) {
        Log.i(TAG, "Stop timeOut")
        timeoutClock.stopTimeout()
        timeoutClock = null
      }
    }
    def actionStartBeep(): Unit = { uiListener.startBeeping() }
    def actionStopBeep(): Unit = { uiListener.stopBeeping() }
    def actionBeepOnce(): Unit = { uiListener.startBeepOnce() }
    // known states
    private val STOPPED = new TimerWatchState {
      override def getStateName(): Int = R.string.STOPPED
      override def updateView(): Unit = { updateUIRuntime(); enableButton() }
      override def onButtonPress(): Unit = { if(timeModel.isValid()) toRunningState() }
      //override def onButtonPress(): Unit = { actionInc(); actionRestartTimeout(this) }
      //def onResumeStoppedState(): Unit = { actionRestartTimeout(this) }
      //override def restartTimeout: Unit = { actionRestartTimeout(this) }
      override def getStateButtonAction(): Int = R.string.START

      //override def onTimeout(): Unit = toRunningState()

      override def onEntry(): Unit = {
        /*if(resumeStateFlag) {
          actionRestartTimeout(this)
        }
        setResumeStateFlag(false)*/
        Log.i(TAG, "STOPPED entry")
        enableButton()
        updateUIRuntime()
      }
      override def onExit(): Unit = {
        //actionStopTimeout()
        //disableButton()
        Log.i(TAG, "STOPPED exit")
      }

    }

    private val STOPPED_COUNTING = new TimerWatchState {

      override def getStateButtonAction(): Int = R.string.START

      // def restartTimeout: Unit
      override def getStateName(): Int = R.string.STOPPED

      override def onExit(): Unit = { actionStopTimeout(); disableButton(); Log.i(TAG, "STOPPED_COUNTING exit") }

      override def updateView(): Unit = updateUIRuntime()

      override def onEntry(): Unit = {actionRestartTimeout(this); Log.i(TAG, "STOPPED_COUNTING entry") }

      override def onButtonPress(): Unit = { if(timeModel.isValid()) toRunningState() }

      override def onTimeout(): Unit = { if(timeModel.isValid()) toRunningState() }
    }

    private val RUNNING = new TimerWatchState {
      override def getStateName(): Int = R.string.RUNNING
      override def updateView(): Unit = updateUIRuntime()
      override def onButtonPress(): Unit = { actionReset(); toStoppedState() }
      override def onTick(): Unit = {
        //timeModel.setResumeFlag(false);
        actionDec()
        if (timeModel.hasTimedOut()){
          toBeepingState()
        }
      }
      //override def restartTimeout: Unit = {}
      override def getStateButtonAction(): Int = R.string.STOP
      override def onEntry(): Unit = {
        Log.i(TAG, "RUNNING entry")
        actionBeepOnce()
        //if(!timeModel.isResume()) actionBeepOnce()
        //if(!timeModel.isResume()) actionStopBeep()

        clockModel.start()
        //disableButton()
      }
      override def onExit(): Unit = { clockModel.stop(); Log.i(TAG, "RUNNING exit") }
    }

    private val BEEPING = new TimerWatchState {
      override def getStateName(): Int = R.string.BEEPING
      override def updateView(): Unit = updateUIRuntime()
      override def onButtonPress(): Unit = { actionReset(); toStoppedState() }
      override def getStateButtonAction(): Int = R.string.STOP
      override def onEntry(): Unit = { enableButton(); actionStartBeep() }
      override def onExit(): Unit = actionStopBeep()
      //override def restartTimeout: Unit = {}
    }
  }

}
