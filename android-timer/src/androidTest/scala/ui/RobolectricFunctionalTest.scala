package ui

import edu.luc.etl.scala.timer.ui.{AbstractFunctionalTest, MainActivity}
import org.junit.runner.RunWith
import org.robolectric.{RobolectricTestRunner, Robolectric}
import org.scalatest.junit.JUnitSuite

/**
 * Created by sauloaguiar on 11/8/14.
 */
@RunWith(classOf[RobolectricTestRunner])
class RobolectricFunctionalTest extends JUnitSuite with AbstractFunctionalTest {

  override protected lazy val activity = Robolectric.buildActivity(classOf[MainActivity]).create().start().get

  override protected def runUiThreadTask(): Unit = Robolectric.runUiThreadTasks()
}
