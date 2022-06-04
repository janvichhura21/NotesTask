package com.example.task


import android.view.KeyEvent
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun FragmentActivity.addFragment(
    container: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false
) {
    supportFragmentManager.beginTransaction()
        .add(container, fragment, fragment.javaClass.simpleName)
        .apply {
            if (addToBackStack) addToBackStack(fragment.javaClass.simpleName)
        }
        .commit()
}

fun FragmentActivity.replaceFragment(
    container: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false
) {
    supportFragmentManager.beginTransaction()
        .replace(container, fragment, fragment.javaClass.simpleName)
        .apply {
            if (addToBackStack) addToBackStack(fragment.javaClass.simpleName)
        }
        .commit()
}

fun FragmentActivity.popFragment() {
    supportFragmentManager.popBackStack()
}

fun FragmentActivity.closeFragment(fragment: Fragment, resultCode: Int) {
    supportFragmentManager.beginTransaction().remove(fragment).commitNow()
    val list = supportFragmentManager.fragments.reversed()
    for (f in list) {
        if (f.removePreviousFragmentListener != null) {
            f.removePreviousFragmentListener?.invoke(resultCode)
            break
        }
    }
//    if(list.isNotEmpty()){
//        list.last().removePreviousFragmentListener?.invoke(resultCode)
//        println(list.last().removePreviousFragmentListener)
//        println(list.last()::class.java.simpleName)
//    }
}

fun FragmentActivity.getProgressBar(cancellable: Boolean): AlertDialog {
    val progressDialog: AlertDialog
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle("Loading...")
    val progressBar = ProgressBar(this)
    val lp = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    progressBar.layoutParams = lp
    builder.setCancelable(cancellable)
    builder.setView(progressBar)
    progressDialog = builder.create()
    return progressDialog
}


/**
 * This method will set error feature on the edit text field.
 * Feature to show error in case of empty field and if user
 * enters some data then error is removed.
 */
fun TextInputLayout.setRequired() {
    // This listener will watch the edit text field
    // for showing and removing errors
    editText?.addTextChangedListener {
        if (it.isNullOrBlank()) {
            this.error = "Required"
        } else {
            this.error = null
        }
    }
}

fun TextInputEditText.setRequired() {

    if (editableText.isNullOrBlank()) {
        this.error = "Required"
    } else {
        this.error = null
    }
}
fun EditText.setRequired() {

    if (editableText.isNullOrBlank()) {
        this.error = "Required"
    } else {
        this.error = null
    }
}


/**
 * For passing data object as extra
 */




fun Fragment.addBackButtonListener(listener: () -> Unit) {
    view?.isFocusableInTouchMode = true
    view?.requestFocus()
    view?.setOnKeyListener { _, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            listener()
            return@setOnKeyListener true
        }

        return@setOnKeyListener false
    }
}

var listeners = mutableMapOf<Int, ((resultCode: Int) -> Unit)?>()
var Fragment.removePreviousFragmentListener: ((resultCode: Int) -> Unit)?
    get() = listeners[this.hashCode()]
    set(value) {
        listeners[this.hashCode()] = value
    }

fun Fragment.setOnRemovePreviousFragmentListener(listener: (resultCode: Int) -> Unit) {
    removePreviousFragmentListener = listener
}

fun Fragment.showAlterDialog(message: String) {
    AlertDialog.Builder(requireContext())
        .setMessage(message)
        .setPositiveButton(
            "OK"
        ) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}

