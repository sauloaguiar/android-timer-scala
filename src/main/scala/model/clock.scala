package edu.luc.etl.cs313.scala.stopwatch
package model

import java.util.{TimerTask, Timer}

object clock {

  /** A listener for onTick events coming from the internal clock model.  */
  trait OnTickListener { def onTick(): Unit }

  /** The active model of the internal clock that periodically emits tick events. */
  trait ClockModel { //extends OnTickSource {
    def start(): Unit
    def stop(): Unit
  }

  /**
   * An implementation of the internal clock.
   * The argument is passed by name for safely setting up a mutual dependency.
   */
  class DefaultClockModel(listener: => OnTickListener) extends ClockModel {

    // TODO make accurate by keeping track of partial seconds when canceled etc.

    private var timer: Timer = _

//    private var listener: OnTickListener = _
//
//    override def setOnTickListener(listener: OnTickListener) = this.listener = listener

    override def start() = {
      timer = new Timer()
      // The clock model runs onTick every 1000 milliseconds
      timer.schedule(new TimerTask() {
        override def run() = listener.onTick() // fire event
      }, /*initial delay*/ 1000, /*periodic delay*/ 1000)
    }

    override def stop() = { timer.cancel() ; timer = null }
  }
}
