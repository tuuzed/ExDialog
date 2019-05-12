@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ListsControllerInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder

inline fun <T> ExDialog.Factory.showSimpleItems(windowContext: Context, func: SimpleItemsController<T>.() -> Unit) {
    ExDialog.show(windowContext) { simpleItems(func) }
}

inline fun <T> ExDialog.simpleItems(func: SimpleItemsController<T>.() -> Unit) {
    lists {
        func(SimpleItemsController(this@simpleItems, this))
    }
}

class SimpleItemsController<T>(
    private val dialog: ExDialog,
    private val delegate: ListsController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var callback: ItemsCallback<T>? = null

    init {
        delegate.config { _, listAdapter ->
            listAdapter.bind(SimpleItem::class.java, object : AbstractItemViewBinder<SimpleItem<T>>() {
                override fun getLayoutId(): Int = R.layout.listitem_simpleitems
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: SimpleItem<T>, position: Int) {
                    holder.text(R.id.text, item.data.toString())
                    holder.click(R.id.text) { callback?.invoke(dialog, item.data, position) }
                }
            })
        }
    }

    fun callback(callback: ItemsCallback<T>) {
        this.callback = callback
    }

    fun items(items: List<T>) {
        delegate.items(items.map { SimpleItem(it) })
    }

}

private class SimpleItem<T>(val data: T)

