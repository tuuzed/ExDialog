@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ex.lifecycle

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
class MaterialDialogLifecycleObserver(
    private val dialog: ExDialog
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        dialog.dismiss()
    }

}