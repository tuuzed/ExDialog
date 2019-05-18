package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ListsControllerInterface

fun <T> ExDialog.simpleItems(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    onClickItem: ItemsCallback<T>? = null,
    items: List<T>? = null,
    disableIndices: List<Int>? = null,
    //
    toReadable: ItemToReadable<T> = { it.toString() },
    func: (SimpleItemsController<T>.() -> Unit)? = null
) {
    lists(titleRes, title, iconRes, icon) {
        SimpleItemsController(
            this@simpleItems, this, toReadable
        ).apply {

            onClickItem?.let { onClickItem(it) }
            items?.let { items(items, disableIndices ?: emptyList()) }

            func?.invoke(this)
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

    private inner class ItemViewBinder : DialogListsAdapter.Binder<Item<T>> {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogListsAdapter.ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.listitem_simpleitems, parent, false
            )
            return DialogListsAdapter.ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: DialogListsAdapter.ViewHolder, position: Int, item: Item<T>) {
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
            holder.find<TextView>(R.id.text).text = toReadable(item.data)
            holder.find<View>(R.id.text).setOnClickListener {
                choiceItemCallback?.invoke(
                    dialog,
                    reviseIndex(position),
                    item.data
                )
            }
        }
    }

}




