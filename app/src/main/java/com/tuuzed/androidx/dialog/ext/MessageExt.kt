@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import com.tuuzed.androidx.dialog.ext.interfaces.MessageControllerInterface

inline fun ExDialog.Factory.message(windowContext: Context, func: MessageController.() -> Unit) {
    ExDialog.show(windowContext) { message(func) }
}

inline fun ExDialog.message(func: MessageController.() -> Unit) {
    customView {
        func(MessageController(this@message, this) { customView(it) })
    }
}

class MessageController(
    private val dialog: ExDialog,
    private val delegate: CustomViewController,
    attachView: (View) -> Unit
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    MessageControllerInterface {

    private val messageText: TextView

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.part_dialog_message, null, false)
        messageText = view.findViewById(R.id.messageText)

        attachView(view)
    }

    override fun message(text: CharSequence?) {
        messageText.text = text
    }

    override fun message(@StringRes resId: Int) {
        messageText.setText(resId)
    }

}

