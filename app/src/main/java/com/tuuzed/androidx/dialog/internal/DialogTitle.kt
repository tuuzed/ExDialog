package com.tuuzed.androidx.dialog.internal

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.tuuzed.androidx.dialog.R

class DialogTitle(
    context: Context, attrs: AttributeSet
) : LinearLayout(context, attrs) {

    private val titleIcon: ImageView
    private val titleText: TextView

    init {
        View.inflate(context, R.layout.basic_dialog_layout_part_title, this)
        titleIcon = findViewById(R.id.title_icon)
        titleText = findViewById(R.id.title_text)
    }

    fun setupHideViews() {
        this.visibility = View.GONE
        titleIcon.visibility = View.GONE
        titleText.visibility = View.GONE
    }

    internal fun setIcon(icon: Drawable?) {
        titleIcon.visibility = View.VISIBLE
        icon?.also { titleIcon.setImageDrawable(it) }
    }

    internal fun setText(text: CharSequence?, @ColorInt color: Int?) {
        titleText.visibility = View.VISIBLE
        text?.also { titleText.text = text }
        color?.also { titleText.setTextColor(it) }
    }

}