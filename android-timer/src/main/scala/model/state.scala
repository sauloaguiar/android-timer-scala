package edu.luc.etl.scala.timer
package model

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
    def getStateName(): Int
    def getStateButtonAction(): Int
    def onTimeout(): Unit = throw new UnsupportedOperationException("onTimeout")
    def onTick(): Unit = throw new UnsupportedOperationException("onTick")
  }

  class DefaultTimerWatchStateMachine(
    timeModel: TimeModel,
    clockModel: ClockModel,
    uiListener: TimerUIUpdateListener) extends TimerStateMachine {

    def getCurrentState(): Int = state.getStateName()
    private var state: TimerWatchState = _
    private var timeoutClock: TimeoutModel = _

    protected def setState(newState: TimerWatchState): Unit = {
      if (state != null) state.onExit()
      state = newState
      state.onEntry()
      uiListener.updateState(state.getStateName(), state.getStateButtonAction())
    }

    override def getState(): TimerWatchState = state


    // UI Based Events
    override def onButtonPress(): Unit = state.onButtonPress()
    override def onTick(): Unit = state.onTick()

    // transitions
    def toStoppedState(): Unit = setState(STOPPED)
    def toRunningState(): Unit = setState(RUNNING)
    def toBeepingState(): Unit = setState(BEEPING)

    //
    def updateUIRuntime(): Unit = uiListener.updateTime(timeModel.getRuntime)

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
      if (timeoutClock == null) {
        timeoutClock = new DefaultTimeoutModel(listener)
      }
      timeoutClock.restartTimeout(3)
    }
    def actionStopTimeout(): Unit = {
      if (timeoutClock != null) {
        timeoutClock.stopTimeout()
        timeoutClock = null
      }
    }
    def actionStartBeep(): Unit = { uiListener.startBeeping() }
    def actionStopBeep(): Unit = { uiListener.stopBeeping() }

    // known states
    private val STOPPED = new TimerWatchState {
      override def getStateName(): Int = R.string.STOPPED
      override def updateView(): Unit = updateUIRuntime()
      override def onButtonPress(): Unit = { actionInc(); actionRestartTimeout(this) }
      override def getStateButtonAction(): Int = R.string.INCREMENT

      override def onTimeout(): Unit = toRunningState()

      override def onEntry(): Unit = updateUIRuntime()
      override def onExit(): Unit = actionStopTimeout()
    }

    private val RUNNING = new TimerWatchState {
      override def getStateName(): Int = R.string.RUNNING
      override def updateView(): Unit = updateUIRuntime()
      override def onButtonPress(): Unit = { actionReset(); toStoppedState() }
      override def onTick(): Unit = {
        actionDec()
        if (timeModel.hasTimedOut()){
          toBeepingState()
        }
      }
      override def getStateButtonAction(): Int = R.string.STOP
      override def onEntry(): Unit = clockModel.start()
      override def onExit(): Unit = clockModel.stop()
    }

    private val BEEPING = new TimerWatchState {
      override def getStateName(): Int = R.string.BEEPING
      override def updateView(): Unit = updateUIRuntime()
      override def onButtonPress(): Unit = { actionReset(); toStoppedState() }
      override def getStateButtonAction(): Int = R.string.STOP
      override def onEntry(): Unit = actionStartBeep()
      override def onExit(): Unit = actionStopBeep()
    }
  }

}
