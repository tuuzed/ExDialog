@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import com.tuuzed.androidx.dialog.ExDialog

@SuppressLint("InflateParams")
inline fun ExDialog.customView(func: CustomViewConfigurator.() -> Unit) {
    basic {
        val configurator = CustomViewConfigurator(this@customView, this)
        func(configurator)
    }
}

class CustomViewConfigurator(
    private val dialog: ExDialog,
    private val basicConfigurator: BasicConfigurator
) : DialogConfiguratorInterface by basicConfigurator {

    fun customView(@LayoutRes layoutId: Int) {
        basicConfigurator.customView(layoutId)
    }

    fun customView(view: View) {
        basicConfigurator.customView(view)
    }

}