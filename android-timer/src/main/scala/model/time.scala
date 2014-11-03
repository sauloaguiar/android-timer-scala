package model

/**
 * Created by sauloaguiar on 10/30/14.
 */
object time {

  // Passive data model for the timer
  trait TimeModel {
    def resetRuntime(): Unit
    def incRuntime(): Unit
    def getRuntime(): Int
    def setRuntime(value: Int): Unit
  }

  // Default implementation for the timer
  class DefaultTimeModel extends TimeModel {

    private var runningTime = 0

    override def resetRuntime(): Unit = runningTime = 0
    override def incRuntime(): Unit = runningTime = ( runningTime + 1 ) % 99
    override def setRuntime(value: Int): Unit = runningTime = value
    override def getRuntime(): Int = runningTime
  }
}
