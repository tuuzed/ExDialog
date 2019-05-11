@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.CustomViewControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface

inline fun ExDialog.Factory.customView(windowContext: Context, func: CustomViewController.() -> Unit) {
    ExDialog.show(windowContext) { customView(func) }
}

inline fun ExDialog.customView(func: CustomViewController.() -> Unit) {
    basic { func(CustomViewController(this@customView, this)) }
}

class CustomViewController(
    private val dialog: ExDialog,
    private val delegate: BasicController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    CustomViewControllerInterface {
    override fun customView(@LayoutRes layoutId: Int) = delegate.customView(layoutId)
    override fun customView(view: View) = delegate.customView(view)
}