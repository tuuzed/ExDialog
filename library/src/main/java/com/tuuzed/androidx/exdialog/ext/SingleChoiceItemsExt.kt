@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.widget.RadioButton
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ListsControllerInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder
import com.tuuzed.recyclerview.adapter.RecyclerViewAdapter

@JvmOverloads
inline fun <T> ExDialog.singleChoiceItems(
    noinline toReadable: ItemToReadable<T> = { it.toString() },
    func: SingleChoiceItemsController<T>.() -> Unit
) {
    lists {
        func(SingleChoiceItemsController(this@singleChoiceItems, this, toReadable))
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
    private var itemClickCallback: ItemsCallback<T>? = null

    init {
        delegate.config { _, listAdapter ->
            this.listAdapter = listAdapter
            listAdapter.bind(Space::class.java, SpaceItemViewBinder)
            listAdapter.bind(Item::class.java, ItemViewBinder(listAdapter))
        }
    }

    fun callback(callback: SingleChoiceItemsCallback<T>) {
        this.callback = callback
    }

    fun itemClick(callback: ItemsCallback<T>) {
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
        textRes: Int?,
        text: CharSequence?,
        colorRes: Int?,
        color: Int?,
        iconRes: Int?,
        icon: Drawable?,
        enable: Boolean,
        visible: Boolean,
        click: DialogButtonClick
    ) {
        delegate.positiveButton(textRes, text, colorRes, color, iconRes, icon, enable, visible) {
            callback?.also { callback ->
                val outIndex = intArrayOf(-1)
                val item = getLastCheckedItem(outIndex)
                if (item == null) {
                    callback(dialog, -1, null)
                } else {
                    callback(dialog, reviseIndex(outIndex[0]), item.data)
                }
            }
            click(dialog)
        }
    }

    private fun reviseIndex(index: Int): Int = index - 1

    private fun getLastCheckedItem(outIndex: IntArray): Item<T>? {
        val items = listAdapter?.items ?: return null
        items.forEachIndexed { index, item ->
            if (item is Item<*>) {
                if (item.checked) {
                    outIndex[0] = index
                    @Suppress("UNCHECKED_CAST")
                    return item as Item<T>
                }
            }
        }
        return null
    }

    private class Item<T>(val data: T, var checked: Boolean, val disable: Boolean)

    private inner class ItemViewBinder(
        private val listAdapter: RecyclerViewAdapter
    ) : AbstractItemViewBinder<Item<T>>() {
        private val outIndex = intArrayOf(-1)
        override fun getLayoutId(): Int = R.layout.listitem_singlechoiceitems
        override fun onBindViewHolder(holder: CommonItemViewHolder, item: Item<T>, position: Int) {
            if (item.disable) {
                holder.itemView.also {
                    it.isClickable = false
                    it.isEnabled = false
                    it.alpha = 0.2f
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
                outIndex[0] = -1
                getLastCheckedItem(outIndex)?.also {
                    it.checked = false
                    listAdapter.notifyItemChanged(outIndex[0])
                }
                item.checked = true
                listAdapter.notifyItemChanged(position)
                if (outIndex[0] != position) {
                    itemClickCallback?.invoke(
                        dialog,
                        reviseIndex(position),
                        item.data,
                        item.checked
                    )
                }
            }
        }
    }

}

