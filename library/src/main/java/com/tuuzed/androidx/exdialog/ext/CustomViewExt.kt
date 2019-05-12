@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused")

package com.tuuzed.androidx.exdialog.ext

import android.view.View
import androidx.annotation.LayoutRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface


inline fun ExDialog.customView(func: CustomViewController.() -> Unit) {
    basic { func(CustomViewController(this@customView, this)) }
}

class CustomViewController(
    private val dialog: ExDialog,
    private val delegate: BasicController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate {
    fun customView(@LayoutRes layoutId: Int) = delegate.customView(layoutId)
    fun customView(view: View) = delegate.customView(view)
}