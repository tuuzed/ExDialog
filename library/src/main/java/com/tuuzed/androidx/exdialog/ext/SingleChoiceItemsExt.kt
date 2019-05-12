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
    toReadable: ItemToReadable<T>
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var listAdapter: RecyclerViewAdapter? = null
    private var callback: SingleChoiceItemsCallback<T>? = null

    init {
        delegate.config { _, listAdapter ->
            this.listAdapter = listAdapter
            listAdapter.bind(Space::class.java, SpaceItemViewBinder)
            listAdapter.bind(SingleChoiceItem::class.java, object : AbstractItemViewBinder<SingleChoiceItem<T>>() {
                override fun getLayoutId(): Int = R.layout.listitem_singlechoiceitems
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: SingleChoiceItem<T>, position: Int) {
                    val outIndex = intArrayOf(-1)
                    holder.text(R.id.text, toReadable(item.data))
                    holder.find<RadioButton>(R.id.radio).isChecked = item.checked
                    holder.click(R.id.item_layout) {
                        outIndex[0] = -1
                        val lastCheckedItem = lastCheckedItem(outIndex)
                        lastCheckedItem?.also {
                            lastCheckedItem.checked = false
                            listAdapter.notifyItemChanged(outIndex[0])
                        }
                        item.checked = true
                        listAdapter.notifyItemChanged(position)
                    }
                }
            })
        }
    }

    fun callback(callback: SingleChoiceItemsCallback<T>) {
        this.callback = callback
    }

    private fun reviseIndex(index: Int): Int = index - 1
    fun items(items: List<T>, selectedIndex: Int = -1) {
        delegate.items(
            listOf(
                Space,
                *items.mapIndexed { index, item ->
                    SingleChoiceItem(item, selectedIndex == index)
                }.toTypedArray(),
                Space
            )
        )
    }

    override fun positiveButton(text: CharSequence, color: Int?, icon: Drawable?, click: DialogButtonClick) {
        delegate.positiveButton(text, color, icon) { dialog, which ->
            callback?.also { callback ->
                val outIndex = intArrayOf(-1)
                val item = lastCheckedItem(outIndex)
                item?.also { callback(dialog, reviseIndex(outIndex[0]), it.data) }
            }
            click.invoke(dialog, which)
        }
    }

    private fun lastCheckedItem(outIndex: IntArray): SingleChoiceItem<T>? {
        val items = listAdapter?.items ?: return null
        items.forEachIndexed { index, item ->
            if (item is SingleChoiceItem<*>) {
                if (item.checked) {
                    outIndex[0] = index
                    @Suppress("UNCHECKED_CAST")
                    return item as SingleChoiceItem<T>
                }
            }
        }
        return null
    }


}

private class SingleChoiceItem<T>(val data: T, var checked: Boolean)
