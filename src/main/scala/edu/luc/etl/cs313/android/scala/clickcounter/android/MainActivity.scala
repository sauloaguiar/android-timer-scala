package edu.luc.etl.cs313.android.scala.clickcounter
package android

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.util.Log
import model.mutable.BoundedCounter

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
  override lazy val model = createModelFromClassName

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
   * Creates a model instance from the class name provided as the string value
   * of the external model_class resource.
   */
  protected def createModelFromClassName(): BoundedCounter = {
    // for flexibility, instantiate model based on externally configured class name
    Class.
      forName(getResources().getString(R.string.model_class)).
      asSubclass(classOf[BoundedCounter]).newInstance
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