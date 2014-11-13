package model

import edu.luc.etl.scala.timer.R
import edu.luc.etl.scala.timer.common.TimerUIUpdateListener
import edu.luc.etl.scala.timer.model.clock.ClockModel
import edu.luc.etl.scala.timer.model.state.{DefaultTimerWatchStateMachine, TimerStateMachine}
import edu.luc.etl.scala.timer.model.time.TimeModel
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import org.junit.Assert._

/**
 * Created by sauloaguiar on 11/5/14.
 */
trait AbstractStateModelSpecs extends JUnitSuite {

  // creates an instance of the unified mock object
  def fixtureDependency() = new UnifiedMockDependency

  // creates an instance of the SUT
  def fixtureSUT(dependency: UnifiedMockDependency): TimerStateMachine

  @Test def testPreconditions(): Unit = {
    val dependency = fixtureDependency()
    val model = fixtureSUT(dependency)
    model.actionInit()
    assertEquals(R.string.STOPPED, dependency.getState())
  }

  // Verifies the following scenario: time is 0, press increment 5 times, expect time 5.
  @Test def testScenarioIncrement(): Unit = {
    val dependency = fixtureDependency()
    val model = fixtureSUT(dependency)
    model.actionInit()
    assertEquals(0, dependency.getRuntime())

    // action to increment our timer
    (1 to 5) foreach { _ => model.onButtonPress() }
    assertEquals(5, dependency.getTime())
    assertEquals(5, dependency.getRuntime())
  }

  /* Verifies the following scenario: time is 0, press increment 5 times, check state change,
     wait it counts down, check it has timed out. */
  @Test def testScenarioIncrementAndCountdown(): Unit = {
    val dependency = fixtureDependency()
    val model = fixtureSUT(dependency)
    model.actionInit()
    assertEquals(0, dependency.getRuntime())

    // action to increment our timer
    (1 to 5) foreach { _ => model.onButtonPress() }
    // wait until it changes state
    Thread.sleep(3500)
    assertEquals(model.getState().getStateName(), R.string.RUNNING)

    // wait until it counts
    (1 to 5) foreach { _ => model.onTick() }
    assertTrue(dependency.hasTimedOut())
  }

  /*
    Verifies the following scenario: time is 0, press increment 5 times, check state change,
    wait it counts down, check it has timed out and that is beeping
   */
  @Test def testScenarioBeeping(): Unit = {
    val dependency = fixtureDependency()
    val model = fixtureSUT(dependency)
    model.actionInit()
    assertEquals(0, dependency.getRuntime())

    // action to increment our timer
    (1 to 5) foreach { _ => model.onButtonPress() }
    // wait until it changes state
    Thread.sleep(3500)
    assertEquals(model.getState().getStateName(), R.string.RUNNING)

    // wait until it counts
    (1 to 5) foreach { _ => model.onTick() }
    assertTrue(dependency.hasTimedOut())

    assertEquals(model.getState().getStateName(), R.string.BEEPING)
  }

  /*
    Verifies the following scenario: time is 0, press increment 99 times, check it has reached max and
    that the state has changed.
   */
  @Test def testScenarioReachedMax(): Unit = {
    val dependency = fixtureDependency()
    val model = fixtureSUT(dependency)
    model.actionInit()
    assertEquals(0, dependency.getRuntime())

    // action to increment our timer
    (1 to 99) foreach { _ => model.onButtonPress() }

    assertTrue(dependency.hasReachedMax())
    assertEquals(model.getState().getStateName(), R.string.RUNNING)
  }

}

class DefaultStateModelSpecs extends AbstractStateModelSpecs {
  // creates an instance of the SUT
  override def fixtureSUT(dependency: UnifiedMockDependency): TimerStateMachine =
    new DefaultTimerWatchStateMachine(dependency, dependency, dependency)
}


class UnifiedMockDependency extends TimeModel with ClockModel with TimerUIUpdateListener {

  def getState() = stateID
  def getTime() = timeValue
  def isStarted() = started

  private var runningTime = 0
  private var beeping = false
  private var started = false
  private var stateID = -1
  private var timeValue = -1

  // ClockModel methods
  override def hasReachedMax(): Boolean = runningTime == 99
  override def setRuntime(value: Int): Unit = this.runningTime = value
  override def decRuntime(): Unit = runningTime -= 1
  override def incRuntime(): Unit = runningTime += 1
  override def hasTimedOut(): Boolean = runningTime == 0
  override def resetRuntime(): Unit = runningTime = 0
  override def getRuntime(): Int = runningTime

  // TimeModel events
  override def stop(): Unit = started = false
  override def start(): Unit = started = true

  // TimerUIUpdateListener events
  override def stopBeeping(): Unit = beeping = false
  override def startBeeping(): Unit = beeping = true
  override def updateState(stateId: Int, buttonLabelId: Int): Unit = this.stateID = stateId
  override def updateTime(timeValue: Int): Unit = this.timeValue = timeValue

  override def isResume(): Boolean = true

  override def setResumeFlag(flag: Boolean): Unit = {}

  override def startBeepOnce(): Unit = {}

  override def isValid(): Boolean = true

  override def enableButton(b: Boolean): Unit = {}
}
