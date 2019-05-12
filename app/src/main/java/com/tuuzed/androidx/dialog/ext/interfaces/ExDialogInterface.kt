package com.tuuzed.androidx.dialog.ext.interfaces

import androidx.annotation.StyleRes
import com.tuuzed.androidx.dialog.ExDialog

internal interface ExDialogInterface {
    fun windowAnimations(@StyleRes animationStyle: Int)
    fun cancelable(flag: Boolean)
    fun canceledOnTouchOutside(cancel: Boolean)
    fun onDialogShow(listener: (ExDialog) -> Unit)
    fun onDialogDismiss(listener: (ExDialog) -> Unit)
    fun onDialogCancel(listener: (ExDialog) -> Unit)
}