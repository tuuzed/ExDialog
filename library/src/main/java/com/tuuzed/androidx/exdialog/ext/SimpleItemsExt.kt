@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused")

package com.tuuzed.androidx.exdialog.ext

import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ListsControllerInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder

@JvmOverloads
inline fun <T> ExDialog.simpleItems(
    noinline toReadable: ItemToReadable<T> = { it.toString() },
    func: SimpleItemsController<T>.() -> Unit
) {
    lists {
        func(SimpleItemsController(this@simpleItems, this, toReadable))
    }
}


class SimpleItemsController<T>(
    private val dialog: ExDialog,
    private val delegate: ListsController,
    toReadable: ItemToReadable<T>
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var itemClickCallback: ItemsCallback<T>? = null

    init {
        delegate.config { _, listAdapter ->
            listAdapter.bind(Space::class.java, SpaceItemViewBinder)
            listAdapter.bind(SimpleItem::class.java, object : AbstractItemViewBinder<SimpleItem<T>>() {
                override fun getLayoutId(): Int = R.layout.listitem_simpleitems
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: SimpleItem<T>, position: Int) {
                    holder.text(R.id.text, toReadable(item.data))
                    holder.click(R.id.text) { itemClickCallback?.invoke(dialog, reviseIndex(position), item.data) }
                }
            })
        }
    }


    fun itemClick(callback: ItemsCallback<T>) {
        this.itemClickCallback = callback
    }

    private fun reviseIndex(index: Int): Int = index - 1
    fun items(items: List<T>) {
        delegate.items(
            listOf(
                Space,
                *items.map { SimpleItem(it) }.toTypedArray(),
                Space
            )
        )
    }

}

private class SimpleItem<T>(val data: T)

