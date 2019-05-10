@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.StringRes
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

@SuppressLint("InflateParams")
inline fun ExDialog.message(func: MessageOptions.() -> Unit) {
    basic {
        val messageView = LayoutInflater.from(windowContext).inflate(R.layout.part_dialog_message, null, false)
        val messageText: TextView = messageView.findViewById(R.id.messageText)
        customView(messageView)
        val options = MessageOptions(this@message, messageText, this)
        func(options)
    }
}

class MessageOptions(
    private val dialog: ExDialog,
    private val messageText: TextView,
    private val basicOptions: BasicOptions
) : DialogOptionsInterface by basicOptions {

    fun message(text: CharSequence?) {
        messageText.text = text
    }

    fun message(@StringRes resId: Int) {
        messageText.setText(resId)
    }

}

