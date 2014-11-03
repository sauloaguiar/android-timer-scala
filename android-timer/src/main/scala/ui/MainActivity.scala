package edu.luc.etl.scala.timer
package ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import edu.luc.etl.scala.timer.R

/**
 * Created by sauloaguiar on 10/30/14.
 */
class MainActivity extends Activity with TypedActivity {

    private val TAG = "tag"

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      Log.i(TAG, "onCreate")
      // inject the (implicit) dependency on the view
      setContentView(R.layout.main)
    }

    def onButtonPressed(view: View): Unit = {
      Toast.makeText(getApplicationContext(), "Button Clicked", Toast.LENGTH_SHORT).show()
    }

}
