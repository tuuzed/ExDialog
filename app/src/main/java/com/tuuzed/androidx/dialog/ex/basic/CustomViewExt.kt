@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ex.basic

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import com.tuuzed.androidx.dialog.ExDialog

@SuppressLint("InflateParams")
inline fun ExDialog.customView(func: CustomViewNamespace.() -> Unit) {
    basic {
        val configurator = CustomViewNamespace(this@customView, this)
        func(configurator)
    }
}

class CustomViewNamespace(
    private val dialog: ExDialog,
    private val delegate: BasicNamespace
) : DialogNamespaceInterface by delegate {

    fun customView(@LayoutRes layoutId: Int) {
        delegate.customView(layoutId)
    }

    fun customView(view: View) {
        delegate.customView(view)
    }

}