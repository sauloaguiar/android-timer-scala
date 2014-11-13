package edu.luc.etl.scala.timer
package ui

import java.io.IOException

import android.app.Activity
import android.media.{AudioManager, MediaPlayer, RingtoneManager}
import android.net.Uri
import android.os.Bundle
import android.text.{Editable, TextWatcher}
import android.util.Log
import android.view.{KeyEvent, View}
import edu.luc.etl.scala.timer.common.Constants._
import edu.luc.etl.scala.timer.common.{TimeWatchModel, TimerUIUpdateListener}
import edu.luc.etl.scala.timer.model.ConcreteTimeWatchModelFacade

/**
 * Created by sauloaguiar on 10/30/14.
 */
class MainActivity extends Activity with TypedActivity with TimerUIUpdateListener {


  private val model: TimeWatchModel = new ConcreteTimeWatchModelFacade {
    lazy val listener = MainActivity.this
  }

  private var mediaPlayer: MediaPlayer = _
  private  var defaultRingtone: Uri = _
  private var userUpdated = true

  private val TAG = "TimerWatchAndroid"

  def startWatchingText(): Unit = {
    findView(TR.seconds).addTextChangedListener(new TextWatcher {
      override def beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int): Unit = {}
      override def onTextChanged(s: CharSequence, start: Int, before: Int, count: Int): Unit = {
      }
      override def afterTextChanged(s: Editable): Unit = {
        if(userUpdated){
          if (s.toString.trim.isEmpty) 0
          else model.onTimerChanged(s.toString.trim.toInt)
        }
      }
    })

    findView(TR.seconds).setOnKeyListener(new View.OnKeyListener {
      override def onKey(v: View, keyCode: Int, event: KeyEvent): Boolean = {
        if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
          Log.i(TAG, "enter pressed")
          model.onButtonPress()
          true
        } else false
      }
    })
  }

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)
    startWatchingText()
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
    userUpdated = false
    val tvS = findView(TR.seconds)
    val seconds = time % MAX_VALUE
    tvS.setText((seconds / 10).toString + (seconds % 10).toString)
    userUpdated = true
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
    mediaPlayer.setDataSource(getApplicationContext(), defaultRingtone)
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM)
    mediaPlayer.prepare()
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
      mediaPlayer.start()
    } catch {
      case ex: IOException =>  throw new RuntimeException(ex)
    }
  }

  override def stopBeeping(): Unit = {
    if(mediaPlayer != null){
      mediaPlayer.release()
    }
  }

  override def enableButton(boolean: Boolean): Unit = {
    runOnUiThread {
      findView(TR.seconds).setEnabled(boolean)
    }
  }
}
