package com.tuuzed.androidx.exdialog.ext

import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tuuzed.androidx.exdialog.ExDialog

fun ExDialog.lifecycleOwner(owner: LifecycleOwner): ExDialog =
    lifecycleOwner(owner.lifecycle)

fun ExDialog.lifecycleOwner(lifecycle: Lifecycle): ExDialog {
    lifecycle.addObserver(ExDialogLifecycleObserver(this))
    return this
}

@Keep
private class ExDialogLifecycleObserver(
    private val dialog: ExDialog
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        dialog.dismiss()
    }

}