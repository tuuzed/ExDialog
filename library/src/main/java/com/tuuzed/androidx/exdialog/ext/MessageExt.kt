package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface

fun ExDialog.message(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    @StringRes messageRes: Int? = null, message: CharSequence? = null,
    //
    func: (MessageController.() -> Unit)? = null
) {
    customView(titleRes, title, iconRes, icon) {
        MessageController(this@message, this) {
            customView(it)
        }.apply {
            if (messageRes != null || message != null) {
                message(messageRes, message)
            }
            func?.invoke(this)
        }
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

    fun message(@StringRes textRes: Int? = null, text: CharSequence? = null) {
        textRes?.also { messageText.setText(textRes) }
        text?.also { messageText.text = text }
    }

}

