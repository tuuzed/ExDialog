package com.tuuzed.androidx.exdialog.internal.interfaces

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.github.ybq.android.spinkit.sprite.Sprite

interface ListsControllerInterface {

    fun showLoadingView(
        icon: Sprite? = null,
        @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null
    )

    fun showMessageView(
        @StringRes textRes: Int? = null, text: CharSequence? = null,
        @ColorRes textColorRes: Int? = null, @ColorInt textColor: Int? = null,
        click: () -> Unit
    )

    fun showItemsView()

}