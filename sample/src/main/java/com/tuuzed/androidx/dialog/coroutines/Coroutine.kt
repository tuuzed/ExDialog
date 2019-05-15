package com.tuuzed.androidx.dialog.coroutines

/** 协程工具类 */
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*

fun Job?.safeCancel() = this?.let { if (!it.isCancelled) it.cancel() }

inline fun <T> async(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: () -> T
): Deferred<T> = GlobalScope.async(dispatcher) { block() }

fun Deferred<*>.lifecycleOwner(lifecycleOwner: LifecycleOwner) =
    lifecycleOwner.lifecycle.addObserver(CoroutineLifecycleObserver(this))

infix fun <T> Deferred<T>.then(
    block: (T) -> Unit
): Job = GlobalScope.launch(Dispatchers.Main) { block(this@then.await()) }