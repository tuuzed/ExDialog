package com.tuuzed.androidx.dialog.coroutines

import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Deferred

@Keep
class CoroutineLifecycleObserver(
    private val deferred: Deferred<*>
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        deferred.safeCancel()
    }

}