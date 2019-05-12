@file:Suppress("MemberVisibilityCanBePrivate")

package com.tuuzed.androidx.exdialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.annotation.StyleRes
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface


class ExDialog(
    val windowContext: Context
) : Dialog(windowContext, R.style.Theme_ExDialog), ExDialogInterface {


    companion object Factory {
        const val BUTTON_POSITIVE = -1
        const val BUTTON_NEGATIVE = -2
        const val BUTTON_NEUTRAL = -3

        var WINDOW_ANIMATION_FADE = R.style.ExDialog_Fade_AnimationStyle
            private set
        var WINDOW_ANIMATION_SLIDE = R.style.ExDialog_Slide_AnimationStyle
            private set
    }


    private var windowAnimations: Int? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    inline fun show(func: ExDialog.() -> Unit): ExDialog {
        this.func()
        show()
        return this
    }

    override fun windowAnimations(@StyleRes animationStyle: Int) {
        this.windowAnimations = animationStyle
    }

    override fun cancelable(flag: Boolean) {
        setCancelable(flag)
    }

    override fun canceledOnTouchOutside(cancel: Boolean) {
        setCanceledOnTouchOutside(cancel)
    }

    override fun onDialogShow(listener: (ExDialog) -> Unit) {
        setOnShowListener { listener(this) }
    }

    override fun onDialogDismiss(listener: (ExDialog) -> Unit) {
        setOnDismissListener { listener(this) }
    }

    override fun onDialogCancel(listener: (ExDialog) -> Unit) {
        setOnCancelListener { listener(this) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requireWindow {
            windowAnimations?.also { setWindowAnimations(it) }
        }
    }

    private inline fun requireWindow(func: Window.() -> Unit) = this.window?.func()

}