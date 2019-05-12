package com.tuuzed.androidx.exdialog.internal.interfaces

import androidx.annotation.StyleRes
import com.tuuzed.androidx.exdialog.ExDialog

internal interface ExDialogInterface {
    fun windowAnimations(@StyleRes animationStyle: Int)
    fun cancelable(flag: Boolean)
    fun canceledOnTouchOutside(cancel: Boolean)
    fun onDialogShow(listener: (ExDialog) -> Unit)
    fun onDialogDismiss(listener: (ExDialog) -> Unit)
    fun onDialogCancel(listener: (ExDialog) -> Unit)
}