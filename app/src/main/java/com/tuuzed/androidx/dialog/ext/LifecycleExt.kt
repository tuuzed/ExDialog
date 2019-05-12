@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tuuzed.androidx.dialog.ExDialog

fun ExDialog.lifecycleOwner(owner: LifecycleOwner): ExDialog =
    lifecycleOwner(owner.lifecycle)

fun ExDialog.lifecycleOwner(lifecycle: Lifecycle): ExDialog {
    lifecycle.addObserver(MaterialDialogLifecycleObserver(this))
    return this
}

@Keep
private class MaterialDialogLifecycleObserver(
    private val dialog: ExDialog
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        dialog.dismiss()
    }

}