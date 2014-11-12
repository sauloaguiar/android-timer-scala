package edu.luc.etl.scala.timer
package ui

import android.app.Activity
import android.media.MediaPlayer.OnCompletionListener
import android.media.{AudioManager, MediaPlayer, RingtoneManager}
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import edu.luc.etl.scala.timer.common.Constants._
import edu.luc.etl.scala.timer.common.{TimeWatchMemento, TimeWatchModel, TimerUIUpdateListener}
import edu.luc.etl.scala.timer.model.ConcreteTimeWatchModelFacade
import java.io.IOException

/**
 * Created by sauloaguiar on 10/30/14.
 */
class MainActivity extends Activity with TypedActivity with TimerUIUpdateListener {


  private val model: TimeWatchModel = new ConcreteTimeWatchModelFacade {
    lazy val listener = MainActivity.this
  }

  private var mediaPlayer: MediaPlayer = _
  private  var defaultRingtone: Uri = _

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
    val seconds = time % MAX_VALUE
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
    //val defaultRingtone: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    mediaPlayer.setDataSource(getApplicationContext(), defaultRingtone)
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM)
    mediaPlayer.prepare()
   /* mediaPlayer.setOnCompletionListener(new OnCompletionListener {
      override def onCompletion(mp: MediaPlayer): Unit = mp.release()
    })*/
  }

  override def startBeeping(): Unit = {
    defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    configurePlayer
    mediaPlayer.start()
  }
  override def startBeepOnce(): Unit = {
    playDefaultNotification()

    }

  protected def playDefaultNotification(): Unit = {
    val defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    mediaPlayer = new MediaPlayer

    try {
      mediaPlayer.setDataSource(getApplicationContext, defaultRingtoneUri)
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION)
      mediaPlayer.prepare()
      /*mediaPlayer.setOnCompletionListener(new OnCompletionListener {
        override def onCompletion(mp: MediaPlayer): Unit = { mp.release() }
      })*/
      mediaPlayer.start()
     // mediaPlayer.wait(2000)
     // mediaPlayer.stop()
    } catch {
      case ex: IOException =>  throw new RuntimeException(ex)
    }
  }


  override def stopBeeping(): Unit = {

      //Log.i("TimerWatchAndroid","MEDIPLSYER!!!!!! "+mediaPlayer)
      if(mediaPlayer != null){
        mediaPlayer.release()
        /*if() {
          mediaPlayer.stop()
          mediaPlayer.reset()
        }*/
    }
  }


  private val KEY = "timewatch-memento"



  override def onSaveInstanceState(savedInstanceState: Bundle): Unit = {
    Log.i(TAG, "onSaveInstanceState")
    savedInstanceState.putSerializable(KEY, model.getMemento)
    model.onStop()
    //model.
    /*if (mediaPlayer != null) {
      mediaPlayer.stop()
      mediaPlayer.reset()
  }*/
    stopBeeping()
    super.onSaveInstanceState(savedInstanceState)
  }

  override def onRestoreInstanceState(savedInstanceState: Bundle): Unit = {
    super.onRestoreInstanceState(savedInstanceState)
    Log.i(TAG, "onRestoreInstanceState")
    model.restoreFromMemento(savedInstanceState.getSerializable(KEY).asInstanceOf[TimeWatchMemento])
  }
}
