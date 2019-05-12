package com.tuuzed.androidx.dialog.internal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import com.tuuzed.androidx.dialog.ext.dp

internal class DialogRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val TAG = "DialogRecyclerView"

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        Log.v(TAG, "onScrollChanged")
    }


}