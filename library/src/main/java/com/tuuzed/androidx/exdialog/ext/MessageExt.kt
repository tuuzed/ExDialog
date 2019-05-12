@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.exdialog.ext

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface

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
    BasicControllerInterface by delegate {

    private val messageText: TextView

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.part_dialog_message, null, false)
        messageText = view.findViewById(R.id.messageText)

        attachView(view)
    }

    fun message(@StringRes resId: Int? = null, text: CharSequence? = null) {
        if (resId != null) {
            messageText.setText(resId)
        } else {
            messageText.text = text
        }
    }

}

