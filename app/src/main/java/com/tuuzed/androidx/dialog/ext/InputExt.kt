@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

@SuppressLint("InflateParams")
fun ExDialog.input(func: InputConfigurator.() -> Unit) {
    customView {
        val inflater = LayoutInflater.from(windowContext)
        val view = inflater.inflate(R.layout.part_dialog_input, null, false)
        val configurator = InputConfigurator(this@input, this, view)
        func(configurator)
        customView(view)
    }
}


class InputConfigurator(
    private val dialog: ExDialog,
    private val configurator: CustomViewConfigurator,
    private val view: View
) : DialogConfiguratorInterface by configurator {

    private val editText: EditText = view.findViewById(R.id.editText)

    private var callback: InputDialogCallback? = null

    fun callback(callback: InputDialogCallback) {
        this.callback = callback
    }

    override fun positiveButton(text: CharSequence?, color: Int?, icon: Drawable?, click: ButtonClick) {
        configurator.positiveButton(text, color, icon) { dialog, which ->
            callback?.invoke(editText.text)
            click(dialog, which)
        }
    }


}
