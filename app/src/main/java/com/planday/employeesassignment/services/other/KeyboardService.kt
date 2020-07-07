package com.planday.employeesassignment.services.other

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.util.*


/**
 */
object KeyboardService {
    fun hideKeyboard(view: View?) {
        if (view != null) {
            val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val hideKeyboard = imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Hides the soft keyboard
     */
    fun hideKeyboard(activity: Activity) {
        try {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Shows the soft keyboard
     */
    fun showKeyboard(view: EditText) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val showKeyboard =
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun showKeyboard(activity: Activity, view: EditText) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            view,
            InputMethodManager.SHOW_IMPLICIT
        )
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val showKeyboard =
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Shows the soft keyboard. This method should only be used after giving focus to an EditText field or another mView that is editable with the keyboard
     */
    fun showKeyboard(context: Context?) {
        if (context == null)
            return
        (Objects.requireNonNull(context.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}
