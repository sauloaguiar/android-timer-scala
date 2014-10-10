package edu.luc.etl.cs313.scala.stopwatch
package model

import common.listeners.{StopwatchUIUpdateListener, StopwatchUIListener}
import edu.luc.etl.cs313.scala.stopwatch.ui.R
import time.TimeModel
import clock.{ClockModel, OnTickListener}

object state {

  /**
   * The state machine for the state-based dynamic model of the stopwatch.
   * This interface is part of the State pattern.
   */
  trait StopwatchStateMachine extends StopwatchUIListener with OnTickListener {
    def actionInit(): Unit
  }

  /** A state in a state machine. This interface is part of the State pattern.  */
  trait StopwatchState extends StopwatchUIListener with OnTickListener {
    def updateView(): Unit
    def getId(): Int
  }

  /** An implementation of the state machine for the stopwatch. */
  class DefaultStopwatchStateMachine(
    timeModel: TimeModel,
    clockModel: ClockModel,
    uiUpdateListener: StopwatchUIUpdateListener
  ) extends StopwatchStateMachine {

    /** The current internal state of this adapter component. Required for the State pattern. */
    private var state: StopwatchState = _

    protected def setState(state: StopwatchState): Unit = {
      this.state = state
      uiUpdateListener.updateState(state.getId())
    }

    // forward event uiUpdateListener methods to the current state
    override def onStartStop(): Unit = state.onStartStop()
    override def onLapReset(): Unit  = state.onLapReset()
    override def onTick(): Unit      = state.onTick()

    def updateUIRuntime(): Unit = uiUpdateListener.updateTime(timeModel.getRuntime())
    def updateUILaptime(): Unit = uiUpdateListener.updateTime(timeModel.getLaptime())

    // known states
    private val STOPPED     = new StoppedState(this)
    private val RUNNING     = new RunningState(this)
    private val LAP_RUNNING = new LapRunningState(this)
    private val LAP_STOPPED = new LapStoppedState(this)

    // transitions
    def toRunningState(): Unit    =  setState(RUNNING)
    def toStoppedState(): Unit    =  setState(STOPPED)
    def toLapRunningState(): Unit =  setState(LAP_RUNNING)
    def toLapStoppedState(): Unit =  setState(LAP_STOPPED)

    // actions
    override def actionInit(): Unit = { toStoppedState() ; actionReset() }
    def actionReset(): Unit         = { timeModel.resetRuntime() ; actionUpdateView() }
    def actionStart(): Unit         = { clockModel.start() }
    def actionStop(): Unit          = { clockModel.stop() }
    def actionLap(): Unit           = { timeModel.setLaptime() }
    def actionInc(): Unit           = { timeModel.incRuntime() ; actionUpdateView() }
    def actionUpdateView(): Unit    = { state.updateView() }
  }

  class StoppedState(sm: DefaultStopwatchStateMachine) extends StopwatchState {
    override def onStartStop() = { sm.actionStart() ; sm.toRunningState() }
    override def onLapReset()  = { sm.actionReset() ; sm.toStoppedState() }
    override def onTick()      = throw new UnsupportedOperationException("onTick")
    override def updateView()  = sm.updateUIRuntime()
    override def getId()       = R.string.STOPPED
  }

  class RunningState(sm: DefaultStopwatchStateMachine) extends StopwatchState {
    override def onStartStop() = { sm.actionStop() ; sm.toStoppedState() }
    override def onLapReset()  = { sm.actionLap() ; sm.toLapRunningState() }
    override def onTick()      = { sm.actionInc() ; sm.toRunningState() }
    override def updateView()  = sm.updateUIRuntime()
    override def getId()       = R.string.RUNNING
  }

  class LapRunningState(sm: DefaultStopwatchStateMachine) extends StopwatchState {
    override def onStartStop() = { sm.actionStop() ; sm.toLapStoppedState() }
    override def onLapReset()  = { sm.toRunningState() ; sm.actionUpdateView() }
    override def onTick()      = { sm.actionInc() ; sm.toLapRunningState() }
    override def updateView()  = sm.updateUILaptime()
    override def getId()       = R.string.LAP_RUNNING
  }

  class LapStoppedState(sm: DefaultStopwatchStateMachine) extends StopwatchState {
    override def onStartStop() = { sm.actionStart() ; sm.toLapRunningState() }
    override def onLapReset()  = { sm.toStoppedState() ; sm.actionUpdateView() }
    override def onTick()      = throw new UnsupportedOperationException("onTick")
    override def updateView()  = sm.updateUILaptime()
    override def getId()       = R.string.LAP_STOPPED
  }
}