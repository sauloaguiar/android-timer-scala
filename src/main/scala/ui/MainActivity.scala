package edu.luc.etl.cs313.scala.stopwatch
package ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import common.{Constants, StopwatchUIUpdateListener, StopwatchModel}
import model.ConcreteStopwatchModelFacade

/**
 * The main Android activity, which provides the required lifecycle methods.
 * By mixing in the abstract Adapter behavior, this class serves as the Adapter
 * in the Model-View-Adapter pattern. It connects the Android GUI view with the
 * model. The model implementation is configured externally via the resource
 * R.string.model_class.
 */
class MainActivity extends Activity with TypedActivity with StopwatchUIUpdateListener {

  private def TAG = "stopwatch-android-activity"

  /**
   * The model on which this activity depends. The model also depends on
   * this activity; we inject this dependency using abstract member override.
   */
  private val model: StopwatchModel = new ConcreteStopwatchModelFacade {
    lazy val listener = MainActivity.this
  }

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)
  }

  override def onStart() {
    super.onStart()
    Log.i(TAG, "onStart")
    model.onStart()
  }

  override def onStop(): Unit = {
    super.onStop()
    Log.i(TAG, "onStart")
    model.onStop()
  }

  override def onSaveInstanceState(savedInstanceState: Bundle): Unit = {
    super.onSaveInstanceState(savedInstanceState)
  }

  override def onRestoreInstanceState(savedInstanceState: Bundle): Unit = {
    super.onRestoreInstanceState(savedInstanceState)
  }

  // TODO remaining lifecycle methods - especially support for rotation

  /**
   * Forwards the semantic ``onStartStop`` event to the model.
   * (Semantic as opposed to, say, a concrete button press.)
   * This and similar events are connected to the
   * corresponding onClick events (actual button presses) in the view itself,
   * usually with the help of the graphical layout editor; the connection also
   * shows up in the XML source of the view layout.
   */
  def onStartStop(view: View) = model.onStartStop()

  /** Forwards the semantic ``onLapReset`` event to the model. */
  def onLapReset(view: View): Unit = model.onLapReset()

  /** Wraps a block of code in a Runnable and runs it on the UI thread. */
  def runOnUiThread(block: => Unit): Unit =
    runOnUiThread(new Runnable() { override def run(): Unit = block })

  /**
   * Updates the seconds and minutes in the UI. It is this UI adapter's
   * responsibility to schedule these incoming events on the UI thread.
   */
  def updateTime(time: Int): Unit = runOnUiThread {
    val tvS = findView(TR.seconds)
    val tvM = findView(TR.minutes)
    val seconds = time % Constants.SEC_PER_MIN
    val minutes = time / Constants.SEC_PER_MIN
    tvS.setText((seconds / 10).toString + (seconds % 10).toString)
    tvM.setText((minutes / 10).toString + (minutes % 10).toString)
  }

  /**
   * Updates the state name shown in the UI. It is this UI adapter's
   * responsibility to schedule these incoming events on the UI thread.
   */
  def updateState(stateId: Int): Unit = runOnUiThread {
    val stateName = findView(TR.stateName)
    stateName.setText(getString(stateId))
  }
}