@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.widget.CheckBox
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ListsControllerInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder
import com.tuuzed.recyclerview.adapter.RecyclerViewAdapter

@JvmOverloads
inline fun <T> ExDialog.multiChoiceItems(
    noinline toReadable: ItemToReadable<T> = { it.toString() },
    func: MultiChoiceItemsController<T>.() -> Unit
) {
    lists {
        func(MultiChoiceItemsController(this@multiChoiceItems, this, toReadable))
    }
}

class MultiChoiceItemsController<T>(
    private val dialog: ExDialog,
    private val delegate: ListsController,
    toReadable: ItemToReadable<T>
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var listAdapter: RecyclerViewAdapter? = null
    private var callback: MultiChoiceItemsCallback<T>? = null


    init {
        delegate.config { _, listAdapter ->
            this.listAdapter = listAdapter
            listAdapter.bind(Space::class.java, SpaceItemViewBinder)
            listAdapter.bind(MultiChoiceItem::class.java, object : AbstractItemViewBinder<MultiChoiceItem<T>>() {
                override fun getLayoutId(): Int = R.layout.listitem_multichoiceitems
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: MultiChoiceItem<T>, position: Int) {
                    holder.text(R.id.text, toReadable(item.data))
                    holder.find<CheckBox>(R.id.checkbox).isChecked = item.checked
                    holder.click(R.id.item_layout) {
                        item.checked = !item.checked
                        listAdapter.notifyItemChanged(position)
                    }
                }
            })
        }
    }

    fun callback(callback: MultiChoiceItemsCallback<T>) {
        this.callback = callback
    }

    private fun reviseIndex(index: Int): Int = index - 1
    fun items(items: List<T>, indices: List<Int> = emptyList()) {
        delegate.items(
            listOf(
                Space,
                *items.mapIndexed { index, item ->
                    MultiChoiceItem(item, indices.contains(index))
                }.toTypedArray(),
                Space
            )
        )
    }

    override fun positiveButton(text: CharSequence, color: Int?, icon: Drawable?, click: DialogButtonClick) {
        delegate.positiveButton(text, color, icon) { dialog, which ->
            callback?.also { callback ->
                val indices = mutableListOf<Int>()
                val items = mutableListOf<T>()
                listAdapter?.items?.forEachIndexed { index, item ->
                    if (item is MultiChoiceItem<*> && item.checked) {
                        indices.add(reviseIndex(index))
                        @Suppress("UNCHECKED_CAST")
                        items.add(item.data as T)
                    }
                }
                if (items.isNotEmpty()) {
                    callback(dialog, indices, items)
                }
            }
            click.invoke(dialog, which)
        }
    }
}

private class MultiChoiceItem<T>(val data: T, var checked: Boolean)