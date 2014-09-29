package edu.luc.etl.cs313.scala.stopwatch.common

object listeners {

  /** A listener for stopwatch events coming from the UI. */
  trait StopwatchUIListener {
    def onStartStop(): Unit
    def onLapReset(): Unit
  }

  /**
   * A listener for UI update notifications.
   * This interface is typically implemented by the adapter, with the
   * notifications coming from the model.
   */
  trait StopwatchUIUpdateListener {
    def updateTime(timeValue: Int): Unit
    def updateState(stateId: Int): Unit
  }
}