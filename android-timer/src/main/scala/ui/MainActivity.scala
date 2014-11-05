package edu.luc.etl.scala.timer
package ui

import android.app.Activity
import android.media.MediaPlayer.OnCompletionListener
import android.media.{AudioManager, MediaPlayer, RingtoneManager}
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import edu.luc.etl.scala.timer.common.{TimeWatchModel, TimerUIUpdateListener}
import edu.luc.etl.scala.timer.R
import model.ConcreteTImeWatchModelFacade

/**
 * Created by sauloaguiar on 10/30/14.
 */
class MainActivity extends Activity with TypedActivity with TimerUIUpdateListener {


  private val model: TimeWatchModel = new ConcreteTImeWatchModelFacade {
    lazy val listener = MainActivity.this
  }

  private var mediaPlayer: MediaPlayer = _

  private val TAG = "TimerWatchAndroid"
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)
  }

  override def onStart(): Unit = {
    super.onStart()
    Log.i(TAG, "onStart")
    model.onStart()
  }

  def onButtonPressed(view: View): Unit = {
    model.onButtonPress()
  }

  /** Wraps a block of code in a Runnable and runs it on the UI thread. */
  def runOnUiThread(block: => Unit): Unit =
    runOnUiThread(new Runnable() { override def run(): Unit = block })


  override def updateTime(time: Int): Unit = runOnUiThread {
    val tvS = findView(TR.seconds)
    val seconds = time % Constants.SEC_PER_MIN
    tvS.setText((seconds / 10).toString + (seconds % 10).toString)
  }

  /**
   * Updates the state name shown in the UI. It is this UI adapter's
   * responsibility to schedule these incoming events on the UI thread.
   */
  def updateState(stateId: Int, buttonLabelId: Int): Unit = runOnUiThread {
    val stateName = findView(TR.stateName)
    val buttonName = findView(TR.controlButton)
    stateName.setText(getString(stateId))
    buttonName.setText(getString(buttonLabelId))
  }

  /* Media Player Methods for playing sounds */
  def configurePlayer {
    mediaPlayer = new MediaPlayer()
    val defaultRingtone: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    mediaPlayer.setDataSource(getApplicationContext(), defaultRingtone)
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM)
    mediaPlayer.prepare()
    mediaPlayer.setOnCompletionListener(new OnCompletionListener {
      override def onCompletion(mp: MediaPlayer): Unit = mediaPlayer.release()
    })
  }

  override def startBeeping(): Unit = {
    configurePlayer
    mediaPlayer.start()
  }

  override def stopBeeping(): Unit = mediaPlayer.stop()
}
