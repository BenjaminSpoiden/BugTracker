package com.ben.bugtrackerclient.utils

import android.view.View

fun View.visible(isVisible: Boolean) {
    visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun View.enabled(_isEnabled: Boolean) {
    isEnabled = _isEnabled
    alpha = if(_isEnabled) 1f else 0.5f
    isClickable = _isEnabled
}