@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ListsControllerInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder

fun <T> ExDialog.simpleItems(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    onClickItem: ItemsCallback<T>? = null,
    items: List<T> = emptyList(),
    disableIndices: List<Int> = emptyList(),
    toReadable: ItemToReadable<T> = { it.toString() },
    func: (SimpleItemsController<T>.() -> Unit)? = null
) {
    lists(titleRes, title, iconRes, icon) {

        SimpleItemsController(this@simpleItems, this, toReadable).also {

            it.onClickItem(onClickItem)
            it.items(items, disableIndices)

            func?.invoke(it)
        }
    }
}


class SimpleItemsController<T>(
    private val dialog: ExDialog,
    private val delegate: ListsController,
    private val toReadable: ItemToReadable<T>
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var choiceItemCallback: ItemsCallback<T>? = null

    init {
        delegate.config { _, listAdapter ->
            listAdapter.bind(Space::class.java, SpaceItemViewBinder)
            listAdapter.bind(Item::class.java, ItemViewBinder())
        }
    }

    fun onClickItem(callback: ItemsCallback<T>?) {
        this.choiceItemCallback = callback
    }

    fun items(items: List<T>, disableIndices: List<Int> = emptyList()) {
        delegate.items(
            listOf(
                Space,
                *items.mapIndexed { index, item ->
                    Item(item, disableIndices.contains(index))
                }.toTypedArray(),
                Space
            )
        )
    }

    private fun reviseIndex(index: Int): Int = index - 1

    private class Item<T>(val data: T, val disable: Boolean)

    private inner class ItemViewBinder : AbstractItemViewBinder<Item<T>>() {
        override fun getLayoutId(): Int = R.layout.listitem_simpleitems
        override fun onBindViewHolder(holder: CommonItemViewHolder, item: Item<T>, position: Int) {
            if (item.disable) {
                holder.itemView.also {
                    it.isClickable = false
                    it.isEnabled = false
                    it.alpha = 0.5f
                }
            } else {
                holder.itemView.also {
                    it.isClickable = true
                    it.isEnabled = true
                    it.alpha = 1.0f
                }
            }
            holder.text(R.id.text, toReadable(item.data))
            holder.click(R.id.text) {
                choiceItemCallback?.invoke(
                    dialog,
                    reviseIndex(position),
                    item.data
                )
            }
        }
    }

}




