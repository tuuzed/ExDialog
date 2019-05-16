package com.tuuzed.androidx.exdialog.ext

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface

fun ExDialog.loading(
    icon: Sprite? = null, @ColorRes iconColorRes: Int? = null, @ColorInt iconColor: Int? = null,
    text: String? = null, @ColorRes textColorRes: Int? = null, @ColorInt textColor: Int? = null,
    //
    func: (LoadingController.() -> Unit)? = null
) {
    LoadingController(this) {
        setContentView(it)
    }.also {
        if (icon != null) {
            it.icon(icon, iconColorRes, iconColor)
        }
        if (text != null) {
            it.text(text, textColorRes, textColor)
        }
        func?.invoke(it)
    }
}

class LoadingController(
    private val dialog: ExDialog,
    attachView: (View) -> Unit
) : ExDialogInterface by dialog {

    private val loadingIcon: SpinKitView
    private val loadingText: TextView

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.loading_dialog_layout, null, false)
        loadingIcon = view.findViewById(R.id.loadingIcon)
        loadingText = view.findViewById(R.id.loadingText)
        loadingText.visibility = View.GONE
        attachView(view)
    }

    fun icon(icon: Sprite? = null, @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null) {
        icon?.also { loadingIcon.setIndeterminateDrawable(it) }
        colorRes?.also { loadingIcon.setColor(resColor(colorRes)) }
        color?.also { loadingIcon.setColor(it) }
    }

    fun text(text: String? = null, @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null) {
        loadingText.visibility = View.VISIBLE
        text?.also { loadingText.text = text }
        colorRes?.also { loadingText.setTextColor(resColor(colorRes)) }
        color?.also { loadingText.setTextColor(it) }
    }

    private fun resColor(@ColorRes colorRes: Int): Int =
        ResourcesCompat.getColor(dialog.windowContext.resources, colorRes, dialog.windowContext.theme)

}
