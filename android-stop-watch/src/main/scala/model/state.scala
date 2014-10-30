package edu.luc.etl.cs313.scala.stopwatch
package model

import common.{StopwatchUIUpdateListener, StopwatchUIListener}
import edu.luc.etl.cs313.scala.stopwatch.ui.R
import time.TimeModel
import clock.{ClockModel, OnTickListener}

/** Contains the components of the dynamic state machine model. */
object state {

  trait Initializable { def actionInit(): Unit }

  /**
   * The state machine for the state-based dynamic model of the stopwatch.
   * This interface is part of the State pattern.
   */
  trait StopwatchStateMachine extends StopwatchUIListener with OnTickListener with Initializable {
    def getState(): StopwatchState
    def actionUpdateView(): Unit
  }

  /** A state in a state machine. This interface is part of the State pattern. */
  trait StopwatchState extends StopwatchUIListener with OnTickListener {
    def updateView(): Unit
    def getId(): Int
  }

  /** An implementation of the state machine for the stopwatch. */
  class DefaultStopwatchStateMachine(
    timeModel: TimeModel,
    clockModel: ClockModel,
    uiUpdateListener: StopwatchUIUpdateListener
  ) extends StopwatchStateMachine with Serializable {

    /** The current internal state of this adapter component. Part of the State pattern. */
    private var state: StopwatchState = _

    protected def setState(state: StopwatchState): Unit = {
      this.state = state
      uiUpdateListener.updateState(state.getId)
    }

    def getState(): StopwatchState = state

    // forward event uiUpdateListener methods to the current state
    override def onStartStop(): Unit = state.onStartStop()
    override def onLapReset(): Unit  = state.onLapReset()
    override def onTick(): Unit      = state.onTick()

    def updateUIRuntime(): Unit = uiUpdateListener.updateTime(timeModel.getRuntime)
    def updateUILaptime(): Unit = uiUpdateListener.updateTime(timeModel.getLaptime)

    // transitions
    def toRunningState(): Unit    =  setState(RUNNING)
    def toStoppedState(): Unit    =  setState(STOPPED)
    def toLapRunningState(): Unit =  setState(LAP_RUNNING)
    def toLapStoppedState(): Unit =  setState(LAP_STOPPED)

    // actions
    override def actionInit(): Unit       = { toStoppedState() ; actionReset() }
    override def actionUpdateView(): Unit = state.updateView()
    def actionReset(): Unit          = { timeModel.resetRuntime() ; actionUpdateView() }
    def actionStart(): Unit          = { clockModel.start() }
    def actionStop(): Unit           = { clockModel.stop() }
    def actionLap(): Unit            = { timeModel.setLaptime() }
    def actionInc(): Unit            = { timeModel.incRuntime() ; actionUpdateView() }

    // known states

    private val STOPPED = new StopwatchState {
      override def onStartStop() = { actionStart() ; toRunningState() }
      override def onLapReset()  = { actionReset() ; toStoppedState() }
      override def onTick()      = throw new UnsupportedOperationException("onTick")
      override def updateView()  = updateUIRuntime()
      override def getId()       = R.string.STOPPED
    }

    private val RUNNING = new StopwatchState {
      override def onStartStop() = { actionStop() ; toStoppedState() }
      override def onLapReset()  = { actionLap() ; toLapRunningState() }
      override def onTick()      = { actionInc() ; toRunningState() }
      override def updateView()  = updateUIRuntime()
      override def getId()       = R.string.RUNNING
    }

    private val LAP_RUNNING = new StopwatchState {
      override def onStartStop() = { actionStop() ; toLapStoppedState() }
      override def onLapReset()  = { toRunningState() ; actionUpdateView() }
      override def onTick()      = { actionInc() ; toLapRunningState() }
      override def updateView()  = updateUILaptime()
      override def getId()       = R.string.LAP_RUNNING
    }

    private val LAP_STOPPED = new StopwatchState {
      override def onStartStop() = { actionStart() ; toLapRunningState() }
      override def onLapReset()  = { toStoppedState() ; actionUpdateView() }
      override def onTick()      = throw new UnsupportedOperationException("onTick")
      override def updateView()  = updateUILaptime()
      override def getId()       = R.string.LAP_STOPPED
    }
  }
}