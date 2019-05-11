@file:Suppress("MemberVisibilityCanBePrivate")

package com.tuuzed.androidx.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.annotation.StyleRes
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface


class ExDialog(
    val windowContext: Context
) : Dialog(windowContext, R.style.Theme_ExDialog), ExDialogInterface {


    companion object Factory {
        const val BUTTON_POSITIVE = -1
        const val BUTTON_NEGATIVE = -2
        const val BUTTON_NEUTRAL = -3

        const val WINDOW_ANIMATION_FADE = R.style.ExDialog_Fade_AnimationStyle
        const val WINDOW_ANIMATION_SLIDE = R.style.ExDialog_Slide_AnimationStyle

        @JvmStatic
        inline fun show(windowContext: Context, func: ExDialog.() -> Unit) = ExDialog(windowContext).show(func)

    }

    private var windowAnimations: Int? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
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

    override fun onShow(listener: (ExDialog) -> Unit) {
        setOnShowListener { listener(this) }
    }

    override fun onDismiss(listener: (ExDialog) -> Unit) {
        setOnDismissListener { listener(this) }
    }

    override fun onCancel(listener: (ExDialog) -> Unit) {
        setOnCancelListener { listener(this) }
    }

    inline fun show(func: ExDialog.() -> Unit): ExDialog {
        this.func()
        show()
        return this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requireWindow {
            windowAnimations?.also { setWindowAnimations(it) }
        }
    }

    private inline fun requireWindow(func: Window.() -> Unit) = this.window?.func()

}