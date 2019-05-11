@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.dialog.ex.basic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

@SuppressLint("InflateParams")
inline fun ExDialog.message(func: MessageNamespace.() -> Unit) {
    customView {
        val inflater = LayoutInflater.from(windowContext)
        val view = inflater.inflate(R.layout.part_dialog_message, null, false)
        val namespace = MessageNamespace(this@message, this, view)
        func(namespace)
        customView(view)
    }
}

class MessageNamespace(
    private val dialog: ExDialog,
    private val delegate: CustomViewNamespace,
    private val view: View
) : DialogNamespaceInterface by delegate  {

    private val messageText: TextView = view.findViewById(R.id.messageText)

    fun message(text: CharSequence?) {
        messageText.text = text
    }

    fun message(@StringRes resId: Int) {
        messageText.setText(resId)
    }

}

