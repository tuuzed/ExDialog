package com.tuuzed.androidx.exdialog.progressbar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.internal.resolveColor
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

@SuppressLint("InflateParams")
fun ExDialog.progressBar(
    text: CharSequence? = null, @ColorRes textColorRes: Int = View.NO_ID, @ColorInt textColor: Int = -1
) {
    val flag = "ExDialog#progressBar".hashCode()
    contentViewIdentifier = flag

    val customView = LayoutInflater.from(context).inflate(
        R.layout.exdialog_progressbar, null, false
    )
    val progressbar: MaterialProgressBar = customView.findViewById(R.id.progressbar)
    val progressbarText: TextView = customView.findViewById(R.id.progressbar_text)
    if (text == null) {
        progressbarText.visibility = View.GONE
    } else {
        progressbarText.visibility = View.VISIBLE
        progressbarText.text = text
        resolveColor(context, textColorRes, textColor)
            ?.also { progressbarText.setTextColor(it) }
    }
    customView(view = customView)
}