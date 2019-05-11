package com.tuuzed.androidx.dialog.ext.interfaces

import androidx.annotation.StringRes

interface MessageControllerInterface {
    fun message(text: CharSequence?)
    fun message(@StringRes resId: Int)
}