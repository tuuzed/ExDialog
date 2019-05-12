package com.tuuzed.androidx.exdialog.internal

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.ext.dp

internal class DialogTitle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val titleIcon: ImageView
    private val titleText: TextView

    init {

        View.inflate(context, R.layout.basic_dialog_layout_part_title, this)

        setPadding(dp(24f), dp(24f), dp(24f), dp(8f))
        orientation = HORIZONTAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        titleIcon = findViewById(R.id.title_icon)
        titleText = findViewById(R.id.title_text)

        this.visibility = View.GONE
        titleIcon.visibility = View.GONE
        titleText.visibility = View.GONE

    }

    fun setIcon(icon: Drawable) {
        titleIcon.visibility = View.VISIBLE
        icon.also { titleIcon.setImageDrawable(it) }
    }

    fun setIcon(@DrawableRes resId: Int) {
        titleIcon.visibility = View.VISIBLE
        titleIcon.setImageResource(resId)
    }

    fun setText(@StringRes resId: Int?, @ColorInt color: Int?) {
        titleText.visibility = View.VISIBLE
        resId?.also { titleText.setText(resId) }
        color?.also { titleText.setTextColor(it) }
    }

    fun setText(text: CharSequence?, @ColorInt color: Int?) {
        titleText.visibility = View.VISIBLE
        text?.also { titleText.text = text }
        color?.also { titleText.setTextColor(it) }
    }

}