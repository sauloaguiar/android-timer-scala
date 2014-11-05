package edu.luc.etl.scala.timer
package model

import java.util.{TimerTask, Timer}
/**
 * Created by sauloaguiar on 11/3/14.
 */
object clock {

  /** A listener for onTick events coming from the internal clock model.  */
  trait OnTickListener { def onTick(): Unit }

  /** The active model of the internal clock that periodically emits tick events. */
  trait ClockModel {
    def start(): Unit
    def stop(): Unit
  }

  /* A listener for onTimeout event coming from the internal timeout model */
  trait OnTimeoutListener {
    def onTimeout()
  }

  /* The active model of the timeout internal clock that emits the timeout event */
  trait TimeoutModel {
    def restartTimeout(second: Int)
    def stopTimeout()
  }

  /**
   * An implementation of the internal timer
   */
  class DefaultTimeoutModel(listener: => OnTimeoutListener) extends TimeoutModel {

    private val DELAY = 1000
    private var timer: Timer = _

    override def restartTimeout(second: Int): Unit = {
      stopTimeout()
      startTimeout(second)
    }

    def startTimeout(second: Int) {
      timer = new Timer()
      timer.schedule(new TimerTask {
        override def run(): Unit = listener.onTimeout()
      }, second * DELAY)
    }

    override def stopTimeout(): Unit = {
      if ( timer != null ) {
        timer.cancel()
        timer = null
      }
    }
  }

  /**
   * An implementation of the internal clock.
   * The argument is passed by name for safely setting up a mutual dependency.
   */
  class DefaultClockModel(listener: => OnTickListener) extends ClockModel {

    // TODO make accurate by keeping track of partial seconds when canceled etc.

    private val DELAY = 1000

    private var timer: Timer = _

    override def start() = {
      timer = new Timer()
      // The clock model runs onTick every 1000 milliseconds
      timer.schedule(new TimerTask() {
        override def run() = listener.onTick() // fire event
      }, /*initial delay*/ DELAY, /*periodic delay*/ DELAY)
    }

    override def stop() =
      if (timer != null) {
        timer.cancel()
        timer = null
      }
  }
}
