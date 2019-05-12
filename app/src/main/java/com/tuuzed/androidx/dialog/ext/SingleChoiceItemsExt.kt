@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import android.widget.RadioButton
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ListsControllerInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder
import com.tuuzed.recyclerview.adapter.RecyclerViewAdapter

inline fun <T> ExDialog.Factory.showSingleChoiceItems(
    windowContext: Context,
    func: SingleChoiceItemsController<T>.() -> Unit
) {
    ExDialog.show(windowContext) { singleChoiceItems(func) }
}

inline fun <T> ExDialog.singleChoiceItems(func: SingleChoiceItemsController<T>.() -> Unit) {
    lists {
        func(SingleChoiceItemsController(this@singleChoiceItems, this))
    }
}

class SingleChoiceItemsController<T>(
    private val dialog: ExDialog,
    private val delegate: ListsController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var listAdapter: RecyclerViewAdapter? = null
    private var callback: SingleChoiceItemsCallback<T>? = null

    init {
        delegate.config { _, listAdapter ->
            this.listAdapter = listAdapter
            listAdapter.bind(SingleChoiceItem::class.java, object : AbstractItemViewBinder<SingleChoiceItem<T>>() {
                override fun getLayoutId(): Int = R.layout.listitem_singlechoiceitems
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: SingleChoiceItem<T>, position: Int) {
                    holder.text(R.id.text, item.data.toString())
                    holder.find<RadioButton>(R.id.radio).isChecked = item.checked
                    holder.click(R.id.item_layout) {
                        val lastCheckedItemIndex = lastCheckedItemIndex()
                        if (lastCheckedItemIndex != -1) {
                            val lastCheckedItem = listAdapter.items[lastCheckedItemIndex] as SingleChoiceItem<*>
                            lastCheckedItem.checked = false
                            listAdapter.notifyItemChanged(lastCheckedItemIndex)
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

    fun items(items: List<T>) {
        delegate.items(items.map { SingleChoiceItem(it, false) })
    }

    private fun lastCheckedItemIndex(): Int {
        val items = listAdapter?.items ?: return -1
        items.forEachIndexed { index, item ->
            if (item is SingleChoiceItem<*>) {
                if (item.checked) {
                    return index
                }
            }
        }
        return -1
    }

}

private class SingleChoiceItem<T>(val data: T, var checked: Boolean)
