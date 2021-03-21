package com.ben.bugtrackerclient.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.visible(isVisible: Boolean) {
    visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun View.enabled(_isEnabled: Boolean) {
    isEnabled = _isEnabled
    alpha = if(_isEnabled) 1f else 0.5f
    isClickable = _isEnabled
}

fun View.snackBar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Login") {
            it()
        }
    }

    snackbar.show()
}