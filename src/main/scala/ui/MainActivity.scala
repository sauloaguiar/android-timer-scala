package edu.luc.etl.cs313.scala.clickcounter
package ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import model.{DefaultBoundedCounter, BoundedCounter}

/**
 * The main Android activity, which provides the required lifecycle methods.
 * By mixing in the abstract Adapter behavior, this class serves as the Adapter
 * in the Model-View-Adapter pattern. It connects the Android GUI view with the
 * model. The model implementation is configured externally via the resource
 * R.string.model_class.
 */
class MainActivity extends Activity with TypedActivity with AbstractAdapter {

  private def TAG = "clickcounter-android-activity"

  // inject the dependency on the model
  override lazy val model = new DefaultBoundedCounter

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)
  }

  override def onStart() {
    super.onStart()
    Log.i(TAG, "onStart")
    updateView()
  }

  /**
   * Updates the concrete view from the model.
   */
  override protected def updateView()  = {
    import scala.language.postfixOps
    // update display
    findView(TR.textview_value).setText(model.get.toString)
    // afford controls according to model state
    findView(TR.button_increment).setEnabled(! model.isFull)
    findView(TR.button_decrement).setEnabled(! model.isEmpty)
  }
}