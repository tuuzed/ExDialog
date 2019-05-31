package com.tuuzed.androidx.exdialog.loading

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.internal.resolveColor

@SuppressLint("InflateParams")
fun ExDialog.loading(
    icon: Sprite = Wave(), @ColorRes iconColorRes: Int = View.NO_ID, @ColorInt iconColor: Int = -1,
    text: CharSequence? = null, @ColorRes textColorRes: Int = View.NO_ID, @ColorInt textColor: Int = -1
) {

    val flag = "ExDialog#loading".hashCode()
    contentViewIdentifier = flag

    val customView = LayoutInflater.from(context).inflate(
        R.layout.exdialog_loading, null, false
    )
    val loadingIcon: SpinKitView = customView.findViewById(R.id.loading_icon)
    val loadingText: TextView = customView.findViewById(R.id.loading_text)
    loadingIcon.setIndeterminateDrawable(icon)
    resolveColor(context, iconColorRes, iconColor)?.also { loadingIcon.setColor(it) }
    if (text == null) {
        loadingText.visibility = View.GONE
    } else {
        loadingText.visibility = View.VISIBLE
        loadingText.text = text
        resolveColor(context, textColorRes, textColor)?.also { loadingText.setTextColor(it) }
    }
    customView(view = customView)
}