package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface

fun ExDialog.customView(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    @LayoutRes layoutId: Int? = null,
    view: View? = null,
    //
    func: (CustomViewController.() -> Unit)? = null
) {
    basic(titleRes, title, iconRes, icon) {
        CustomViewController(
            this@customView, this
        ).apply {
            layoutId?.let { customView(it) }
            view?.let { customView(it) }

            func?.invoke(this)
        }
    }
}

class CustomViewController(
    private val dialog: ExDialog,
    private val delegate: BasicController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate {
    fun customView(@LayoutRes layoutId: Int) = delegate.customView(layoutId)
    fun customView(view: View) = delegate.customView(view)
}