package com.tuuzed.androidx.dialog.ext.interfaces

import androidx.annotation.StyleRes
import com.tuuzed.androidx.dialog.ExDialog

interface ExDialogInterface {
    fun windowAnimations(@StyleRes animationStyle: Int)
    fun cancelable(flag: Boolean)
    fun canceledOnTouchOutside(cancel: Boolean)
    fun onShow(listener: (ExDialog) -> Unit)
    fun onDismiss(listener: (ExDialog) -> Unit)
    fun onCancel(listener: (ExDialog) -> Unit)
}