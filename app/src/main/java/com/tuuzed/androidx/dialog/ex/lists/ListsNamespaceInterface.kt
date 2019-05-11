package com.tuuzed.androidx.dialog.ex.lists

import androidx.annotation.ColorInt
import com.github.ybq.android.spinkit.sprite.Sprite

internal interface ListsNamespaceInterface {
    fun showLoadingView(icon: Sprite? = null, @ColorInt color: Int? = null)
    fun showClickButton(text: CharSequence, @ColorInt color: Int? = null, click: () -> Unit)
    fun items(items: List<*>)
}