package model

/**
 * Created by Shilpika on 11/12/2014.
 */
import org.junit.Test
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.junit.JUnitSuite
import org.scalatest.mock.MockitoSugar
import edu.luc.etl.scala.timer.R
//import common.StopwatchUIUpdateListener
//import clock.ClockModel
//import state.StopwatchStateMachine
//import time.TimeModel
import edu.luc.etl.scala.timer.common.TimerUIUpdateListener
import edu.luc.etl.scala.timer.model.clock.{OnTimeoutListener, TimeoutModel, ClockModel}
import edu.luc.etl.scala.timer.model.state.{DefaultTimerWatchStateMachine, TimerStateMachine}
import edu.luc.etl.scala.timer.model.time.TimeModel

/**
 * An abstract unit test for the state machine abstraction.
 * This is a unit test of an object with multiple dependencies;
 * we use a unified mock object to satisfy all dependencies
 * and verify that the state machine behaved as expected.
 * This also follows the XUnit Testcase Superclass pattern.
 */
trait AbstractStateModelMockitoSpecs extends JUnitSuite with MockitoSugar {

  trait Dependencies {
    val timeModel: TimeModel
    val clockModel: ClockModel
    //val timeoutClockModel: TimeoutModel
    val uiUpdateListener: TimerUIUpdateListener
    //val onTimeoutListener : OnTimeoutListener

  }

  /** Creates an instance of the SUT. */
  def fixtureSUT(dependencies: Dependencies): TimerStateMachine

  /** Creates instances of the dependencies. */
  def fixtureDependency() = new Dependencies {
    override val timeModel = mock[TimeModel]
    override val clockModel = mock[ClockModel]
   // override val timeoutClockModel = mock[TimeoutModel]
    override val uiUpdateListener = mock[TimerUIUpdateListener]
    //override val onTimeoutListener = mock[OnTimeoutListener]
  }

  /**
   * Verifies that we're initially in the stopped state (and told the listener
   * about it).
   */
  @Test def testPreconditions() = {
    val dep = fixtureDependency()
    val model = fixtureSUT(dep)
    model.actionInit()
    verify(dep.uiUpdateListener).updateState(R.string.STOPPED, R.string.INCREMENT)
    verify(dep.uiUpdateListener,times(2)).updateTime(0)
    verifyNoMoreInteractions(dep.uiUpdateListener, dep.clockModel)
 }

  /**
   * Verifies the following scenario: time is 0, press button 3 times, wait 5+ seconds,
   * expect time 5.
   */
  @Test def testScenarioIncrement(): Unit = {
    val dep = fixtureDependency()
    val model = fixtureSUT(dep)
    val t = 5
    model.actionInit()
    //(1 to 5) foreach { _ => model.onButtonPress() }
    verify(dep.clockModel, never).start()
    verify(dep.timeModel, never).incRuntime()
    verify(dep.uiUpdateListener).updateState(R.string.STOPPED,R.string.INCREMENT)
    // directly invoke the button press event handler methods
    (1 to 5) foreach { _ => model.onButtonPress() }
    verify(dep.timeModel, times(5)).incRuntime()
    verify(dep.uiUpdateListener,times(7)).updateTime(anyInt)
    //verify(model,times(5)).actionRestartTimeout()

    /*(1 to t) foreach { _ => model.onTick() }
    verify(dep.timeModel, times(t)).incRuntime()
    verify(dep.uiUpdateListener, times(t + 1)).updateTime(anyInt)
    verifyNoMoreInteractions(dep.clockModel)*/
  }

  /**
   * Verifies the following scenario: time is 0, press button, on 3sec timeout,
   * start Tick and on final timeout startBeeping on button press stop timeout
   */
  @Test def testScenarioRunOverflow(): Unit = {
    val dep = fixtureDependency()
    val model = fixtureSUT(dep)
    model.actionInit()
    verify(dep.uiUpdateListener,times(2)).updateTime(0)
    verify(dep.uiUpdateListener).updateState(R.string.STOPPED, R.string.INCREMENT)
    verify(dep.timeModel).resetRuntime()
    verify(dep.uiUpdateListener,times(2)).updateTime(0)
    model.onButtonPress()
    verify(dep.timeModel).incRuntime()
    verify(dep.uiUpdateListener, times(3)).updateTime(0)
    when(dep.timeModel.getRuntime()).thenReturn(1)
    model.getState().onTimeout()
    verify(dep.clockModel).start()
    verify(dep.uiUpdateListener).updateState(R.string.RUNNING, R.string.STOP)
    model.getState().onTick()
    //model.onTick()
    verify(dep.timeModel).decRuntime()
    verify(dep.uiUpdateListener, times(4)).updateTime(anyInt)
    verify(dep.timeModel).hasTimedOut()
    when(dep.timeModel.hasTimedOut()).thenReturn(true)

    model.getState().onTick()
    //model.onTick()
    verify(dep.timeModel).decRuntime()
    //verify(dep.uiUpdateListener, times(3)).updateTime(anyInt)
    verify(dep.timeModel).hasTimedOut()
    when(dep.timeModel.hasTimedOut()).thenReturn(true)

    println("OOOOO "+model.getState().getStateName())
    //model.getState().onExit()
    println("OOOO1 "+dep.timeModel.getRuntime())
    verify(dep.uiUpdateListener).startBeeping()
    verify(dep.uiUpdateListener).updateState(R.string.BEEPING, R.string.STOP)
    model.onButtonPress()
    verify(dep.uiUpdateListener).stopBeeping()
    verify(dep.uiUpdateListener).updateState(R.string.STOPPED, R.string.STOP)
  }

  /*@Test def testScenarioRun(): Unit = {
    val dep = fixtureDependency()
    val model = fixtureSUT(dep)
    model.actionInit()
    verify(dep.uiUpdateListener,times(2)).updateTime(0)
    verify(dep.uiUpdateListener).updateState(R.string.STOPPED, R.string.INCREMENT)
    verify(dep.uiUpdateListener,times(2)).updateTime(0)

    verify(dep.uiUpdateListener).updateState(R.string.STOPPED, R.string.INCREMENT)
    (1 to 99) foreach { _ => model.onButtonPress()}
    verify(dep.timeModel,times(99)).incRuntime()
    verify(dep.uiUpdateListener, times(101)).updateTime(anyInt)
    when(dep.timeModel.getRuntime()).thenReturn(99)
    model.getState().onTimeout()
    verify(dep.clockModel).start()
    verify(dep.uiUpdateListener).updateState(R.string.RUNNING, R.string.STOP)
    (1 to 99) foreach{ _ => model.onTick()}
    verify(dep.timeModel,times(99)).decRuntime()
    verify(dep.uiUpdateListener,times(101)).updateTime(anyInt)
    when(dep.timeModel.hasTimedOut()).thenReturn(true)
    verify(dep.uiUpdateListener).startBeeping()
    model.onButtonPress()
    verify(dep.uiUpdateListener).stopBeeping()
    verify(dep.uiUpdateListener).updateState(R.string.STOPPED, R.string.STOP)

  }*/
}
