package com.tuuzed.androidx.exdialog.internal.interfaces

import androidx.annotation.ColorInt
import com.github.ybq.android.spinkit.sprite.Sprite

internal interface ListsControllerInterface {
    fun showLoadingView(icon: Sprite? = null, @ColorInt color: Int? = null)
    fun showMessageView(text: CharSequence, @ColorInt color: Int? = null, click: () -> Unit)
    fun showItemsView()
}