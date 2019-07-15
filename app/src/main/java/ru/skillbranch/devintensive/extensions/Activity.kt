package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
//import androidx.fragment.app.Fragment

/**
 * from https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin#44500926
 */
//fun Fragment.hideKeyboard() {
//    view?.let { activity?.hideKeyboard(it) }
//}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}