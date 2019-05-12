@file:Suppress("unused", "CanBeParameter", "InflateParams")


package com.tuuzed.androidx.dialog.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface

inline fun ExDialog.Factory.showLoading(windowContext: Context, func: LoadingController.() -> Unit) {
    ExDialog.show(windowContext) { loading(func) }
}

inline fun ExDialog.loading(func: LoadingController.() -> Unit) {
    func(LoadingController(this) { setContentView(it) })
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

    fun icon(icon: Sprite? = null, @ColorInt color: Int? = null) {
        color?.also { loadingIcon.setColor(it) }
        icon?.also { loadingIcon.setIndeterminateDrawable(it) }
    }

    fun text(text: String? = null, @ColorInt color: Int? = null) {
        loadingText.visibility = View.VISIBLE
        text?.also { loadingText.text = text }
        color?.also { loadingText.setTextColor(it) }
    }

}
