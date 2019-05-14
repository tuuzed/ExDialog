package com.tuuzed.androidx.exdialog.internal

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.ext.dp

class DialogTitle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val titleIcon: ImageView
    val titleText: TextView


    init {

        View.inflate(context, R.layout.basic_dialog_layout_part_title, this)

        setPadding(dp(24f), dp(24f), dp(24f), dp(8f))
        orientation = HORIZONTAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        titleIcon = findViewById(R.id.title_icon)
        titleText = findViewById(R.id.title_text)

    }

    fun requestUpdateLayout() {
        if (titleIcon.visibility == View.VISIBLE ||
            titleText.visibility == View.VISIBLE
        ) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

}