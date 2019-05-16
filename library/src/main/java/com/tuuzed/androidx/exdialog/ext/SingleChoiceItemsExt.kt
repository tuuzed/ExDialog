package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.widget.RadioButton
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ListsControllerInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder
import com.tuuzed.recyclerview.adapter.RecyclerViewAdapter

fun <T> ExDialog.singleChoiceItems(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    onSelectedItemChanged: SingleChoiceItemsCallback<T>? = null,
    callback: SingleChoiceItemsCallback<T>? = null,
    items: List<T>? = null,
    selectedIndex: Int? = null,
    disableIndices: List<Int>? = null,
    //
    toReadable: ItemToReadable<T> = { it.toString() },
    func: (SingleChoiceItemsController<T>.() -> Unit)? = null
) {
    lists(titleRes, title, iconRes, icon) {
        SingleChoiceItemsController(
            this@singleChoiceItems, this, toReadable
        ).apply {

            onSelectedItemChanged?.let { onSelectedItemChanged(it) }
            callback?.let { callback(it) }
            items?.let { items(it, selectedIndex ?: -1, disableIndices ?: emptyList()) }

            func?.invoke(this)
        }
    }
}

class SingleChoiceItemsController<T>(
    private val dialog: ExDialog,
    private val delegate: ListsController,
    private val toReadable: ItemToReadable<T>
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var listAdapter: RecyclerViewAdapter? = null
    private var callback: SingleChoiceItemsCallback<T>? = null
    private var itemClickCallback: SingleChoiceItemsCallback<T>? = null

    init {
        delegate.config { _, listAdapter ->
            this.listAdapter = listAdapter
            listAdapter.bind(Space::class.java, SpaceItemViewBinder)
            listAdapter.bind(Item::class.java, ItemViewBinder(listAdapter))
        }
    }

    fun callback(callback: SingleChoiceItemsCallback<T>?) {
        this.callback = callback
    }

    fun onSelectedItemChanged(callback: SingleChoiceItemsCallback<T>?) {
        this.itemClickCallback = callback
    }

    fun items(items: List<T>, selectedIndex: Int = -1, disableIndices: List<Int> = emptyList()) {
        delegate.items(
            listOf(
                Space,
                *items.mapIndexed { index, item ->
                    Item(item, selectedIndex == index, disableIndices.contains(index))
                }.toTypedArray(),
                Space
            )
        )
    }

    override fun positiveButton(
        textRes: Int?, text: CharSequence?,
        iconRes: Int?, icon: Drawable?,
        colorRes: Int?, color: Int?,
        click: DialogButtonClick?
    ) {
        itemClickCallback?.also { callback ->
            getLastCheckedItem { index, checkedItem ->
                if (checkedItem == null) {
                    callback(dialog, -1, null)
                } else {
                    callback(dialog, index, checkedItem.data)
                }
            }
        }
        delegate.positiveButton(textRes, text, iconRes, icon, colorRes, color) {
            callback?.also { callback ->
                getLastCheckedItem { index, checkedItem ->
                    if (checkedItem == null) {
                        callback(dialog, -1, null)
                    } else {
                        callback(dialog, index, checkedItem.data)
                    }
                }
            }
            click?.invoke(dialog)
        }
    }


    private fun reviseIndex(index: Int): Int = index - 1

    private fun getLastCheckedItem(receiver: (index: Int, checkedItem: Item<T>?) -> Unit) {
        val items = listAdapter?.items
        if (items == null) {
            receiver(-1, null)
        } else {
            items.forEachIndexed { index, item ->
                if (item is Item<*>) {
                    if (item.checked) {
                        @Suppress("UNCHECKED_CAST")
                        receiver(index, item as Item<T>)
                        return
                    }
                }
            }
            receiver(-1, null)
        }
    }

    private class Item<T>(val data: T, var checked: Boolean, val disable: Boolean)

    private inner class ItemViewBinder(
        private val listAdapter: RecyclerViewAdapter
    ) : AbstractItemViewBinder<Item<T>>() {
        override fun getLayoutId(): Int = R.layout.listitem_singlechoiceitems
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
            holder.find<RadioButton>(R.id.radio).isChecked = item.checked
            holder.click(R.id.item_layout) {
                var lastIndex: Int = -1
                getLastCheckedItem { index, checkedItem ->
                    lastIndex = index
                    if (checkedItem != null) {
                        checkedItem.checked = false
                        listAdapter.notifyItemChanged(index)
                    }
                }
                item.checked = true
                listAdapter.notifyItemChanged(position)
                if (lastIndex != position) {
                    itemClickCallback?.invoke(
                        dialog,
                        reviseIndex(position),
                        item.data
                    )
                }
            }
        }
    }

}

