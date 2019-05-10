@file:Suppress("MemberVisibilityCanBePrivate")

package com.tuuzed.androidx.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.annotation.StyleRes


class ExDialog(
    val windowContext: Context
) : Dialog(windowContext, R.style.ExDialogTheme) {


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

    fun windowAnimations(@StyleRes animationStyle: Int): ExDialog {
        this.windowAnimations = animationStyle
        return this
    }

    fun cancelable(flag: Boolean): ExDialog {
        setCancelable(flag)
        return this
    }

    fun canceledOnTouchOutside(cancel: Boolean): ExDialog {
        setCanceledOnTouchOutside(cancel)
        return this
    }

    inline fun onShowEvent(crossinline listener: (ExDialog) -> Unit): ExDialog {
        setOnShowListener { listener(this) }
        return this
    }

    inline fun onDismissEvent(crossinline listener: (ExDialog) -> Unit): ExDialog {
        setOnDismissListener { listener(this) }
        return this
    }

    inline fun onCancelEvent(crossinline listener: (ExDialog) -> Unit): ExDialog {
        setOnCancelListener { listener(this) }
        return this
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