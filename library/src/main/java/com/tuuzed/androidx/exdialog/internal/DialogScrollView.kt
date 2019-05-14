package com.tuuzed.androidx.exdialog.internal

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ScrollView

class DialogScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {
    private val TAG = "DialogScrollView"

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        Log.v(TAG, "onScrollChanged")
    }

}