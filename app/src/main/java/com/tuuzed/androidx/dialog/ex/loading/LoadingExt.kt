@file:Suppress("unused", "CanBeParameter")


package com.tuuzed.androidx.dialog.ex.loading

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
inline fun ExDialog.loading(func: LoadingConfigurator.() -> Unit) {
    val inflater = LayoutInflater.from(windowContext)
    val view = inflater.inflate(R.layout.loading_dialog_layout, null, false)
    val configurator = LoadingConfigurator(view)
    func(configurator)
    setContentView(view)
}

class LoadingConfigurator(
    private val view: View
) {

    private val loadingIcon: SpinKitView = view.findViewById(R.id.loadingIcon)
    private val loadingText: TextView = view.findViewById(R.id.loadingText)

    init {
        loadingText.visibility = View.GONE
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
