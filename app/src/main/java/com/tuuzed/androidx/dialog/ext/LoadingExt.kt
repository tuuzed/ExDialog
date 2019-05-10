@file:Suppress("unused")


package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

@SuppressLint("InflateParams")
inline fun ExDialog.loading(func: LoadingOptions.() -> Unit) {
    val inflater = LayoutInflater.from(windowContext)
    val contentView = inflater.inflate(R.layout.loading_dialog_layout, null, false)

    val loadingIcon: SpinKitView = contentView.findViewById(R.id.loadingIcon)
    val loadingText: TextView = contentView.findViewById(R.id.loadingText)

    loadingText.visibility = View.GONE

    val options = LoadingOptions(loadingIcon, loadingText)
    func(options)
    setContentView(contentView)

}

class LoadingOptions(
    private val loadingIcon: SpinKitView,
    private val loadingText: TextView
) {

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
