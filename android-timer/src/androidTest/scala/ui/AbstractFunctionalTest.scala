package edu.luc.etl.scala.timer
package ui

import android.widget.{Button, TextView}
import org.junit.Assert._
import org.junit.Test

/**
 * Created by sauloaguiar on 11/8/14.
 */
trait AbstractFunctionalTest {

  protected def activity(): MainActivity

  @Test def testActivityExists(): Unit = assertNotNull(activity)

  @Test def testActivityInitialValue(): Unit = { assertEquals(0, getDisplayedValue)}

  @Test def testActivityScenarioIncrement(): Unit = {
    activity.runOnUiThread {
      assertEquals(0, getDisplayedValue())
      (1 to 5) foreach { _ => assertTrue(getButton().performClick()) }

      assertEquals(5, getDisplayedValue())
      assertEquals(activity.getString(R.string.STOPPED), getStateLabel())
    }
  }

  @Test def testActivityScenarioRunning(): Unit = {
    activity.runOnUiThread {
      assertEquals(0, getDisplayedValue())
      (1 to 5) foreach { _ => assertTrue(getButton().performClick()) }

      assertEquals(5, getDisplayedValue())
      assertEquals(activity.getString(R.string.STOPPED), getStateLabel())
    }
    Thread.sleep(3500)
    runUiThreadTask()

    activity.runOnUiThread {
      assertEquals(activity.getString(R.string.RUNNING), getStateLabel())
    }
  }

  @Test def testActivityScenarioCountdown(): Unit = {
    activity.runOnUiThread {
      assertEquals(0, getDisplayedValue())
      (1 to 5) foreach { _ => assertTrue(getButton().performClick()) }

      assertEquals(5, getDisplayedValue())
      assertEquals(activity.getString(R.string.STOPPED), getStateLabel())
    }

    Thread.sleep(3500)
    runUiThreadTask()
    activity.runOnUiThread {
      assertEquals(activity.getString(R.string.RUNNING), getStateLabel())
    }

    Thread.sleep(5500)
    runUiThreadTask()
    activity.runOnUiThread {
      assertEquals(0, getDisplayedValue())
      assertEquals(activity.getString(R.string.BEEPING), getStateLabel())
    }
  }

  @Test def testActivityScenarioReachMax(): Unit = {
    activity.runOnUiThread {
      assertEquals(0, getDisplayedValue())
      (1 to 99) foreach { _ => assertTrue(getButton().performClick()) }

      assertEquals(98, getDisplayedValue())
      assertEquals(activity.getString(R.string.RUNNING), getStateLabel())
    }
    Thread.sleep(500)
    runUiThreadTask()
    activity.runOnUiThread {
      assertEquals(98, getDisplayedValue())
      assertEquals(activity.getString(R.string.RUNNING), getStateLabel())
    }
  }

  @Test def testActivityScenarioStopCounting(): Unit = {
    activity.runOnUiThread {
      assertEquals(0, getDisplayedValue())
      (1 to 5) foreach { _ => assertTrue(getButton().performClick()) }

      assertEquals(5, getDisplayedValue())
      assertEquals(activity.getString(R.string.STOPPED), getStateLabel())
    }

    Thread.sleep(3500)
    runUiThreadTask()
    activity.runOnUiThread {
      assertEquals(activity.getString(R.string.RUNNING), getStateLabel())

      assertTrue(getButton().performClick())
    }

    runUiThreadTask()
    activity.runOnUiThread {
      assertEquals(activity.getString(R.string.STOPPED), getStateLabel())
      assertEquals(0, getDisplayedValue())
    }
  }

  @Test def testActivityScenarioStopBeeping(): Unit = {
    activity.runOnUiThread {
      assertEquals(0, getDisplayedValue())
      (1 to 5) foreach { _ => assertTrue(getButton().performClick()) }

      assertEquals(5, getDisplayedValue())
      assertEquals(activity.getString(R.string.STOPPED), getStateLabel())
    }

    Thread.sleep(3500)
    runUiThreadTask()
    activity.runOnUiThread {
      assertEquals(activity.getString(R.string.RUNNING), getStateLabel())
    }

    Thread.sleep(5500)
    runUiThreadTask()
    activity.runOnUiThread {
      assertEquals(0, getDisplayedValue())
      assertEquals(activity.getString(R.string.BEEPING), getStateLabel())
    }
  }

  def getDisplayedValue(): Int = {
    tvToInt(activity.findView(TR.seconds))
  }

  protected def tvToInt(t: TextView): Int = t.getText.toString.trim.toInt

  protected def getButton(): Button = activity.findView(TR.controlButton)

  protected def getStateLabel(): CharSequence = activity.findView(TR.stateName).getText

  protected def runUiThreadTask(): Unit
}
