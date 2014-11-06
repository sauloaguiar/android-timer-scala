package edu.luc.etl.scala.timer
/**
 * Created by sauloaguiar on 11/3/14.
 */
package object common {
  /**
   * A listener to UI update with notifications.
   * This interface is typically implemented by the adapter, with the
   * notifications coming from the model.
   */
  trait TimerUIUpdateListener {
    def updateTime(timeValue: Int): Unit
    def updateState(stateId: Int, buttonLabelId: Int): Unit
    def startBeeping(): Unit
    def stopBeeping(): Unit
  }

  /** A component with a start/stop lifecycle tied to the app. */
  trait Startable {
    def onStart(): Unit
    def onStop(): Unit
  }

  /**
   * A source for UI update notifications,
   * with a dependency on the corresponding listener.
   */
  trait TimeWatchUIUpdateSource {
    protected val listener: TimerUIUpdateListener
  }

  /** The unified interface of the stopwatch model. */
  trait TimeWatchModel
    extends UIHandling with TimeWatchUIUpdateSource with Startable {
  }

  trait TimeWatchMemento {
    val runTime: Int
    val stateID: Int
  }

  /* Trait for handling UI events */
  trait UIHandling {
    def onButtonPress(): Unit
  }

}
