package com.tuuzed.androidx.exdialog.lifecycle

import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tuuzed.androidx.exdialog.ExDialog

fun ExDialog.lifecycleOwner(owner: LifecycleOwner): ExDialog {
    owner.lifecycle.addObserver(ExDialogLifecycleObserver(this))
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