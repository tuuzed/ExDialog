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

inline fun ExDialog.Factory.singleChoiceItems(windowContext: Context, func: SingleChoiceItemsController.() -> Unit) {
    ExDialog.show(windowContext) { singleChoiceItems(func) }
}

inline fun ExDialog.singleChoiceItems(func: SingleChoiceItemsController.() -> Unit) {
    lists {
        func(SingleChoiceItemsController(this@singleChoiceItems, this))
    }
}

class SingleChoiceItemsController(
    private val dialog: ExDialog,
    private val delegate: ListsController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var listAdapter: RecyclerViewAdapter? = null
    private var callback: SingleChoiceItemsCallback<String>? = null

    init {
        delegate.config { _, listAdapter ->
            this.listAdapter = listAdapter
            listAdapter.bind(SingleChoiceItem::class.java, object : AbstractItemViewBinder<SingleChoiceItem>() {
                override fun getLayoutId(): Int = R.layout.listitem_singlechoiceitems
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: SingleChoiceItem, position: Int) {
                    holder.text(R.id.text, item.data)
                    holder.find<RadioButton>(R.id.radio).isChecked = item.checked
                    holder.click(R.id.item_layout) {
                        val lastCheckedItemIndex = lastCheckedItemIndex()
                        if (lastCheckedItemIndex != -1) {
                            val lastCheckedItem = listAdapter.items[lastCheckedItemIndex] as SingleChoiceItem
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

    private fun lastCheckedItemIndex(): Int {
        val items = listAdapter?.items ?: return -1
        items.forEachIndexed { index, item ->
            if (item is SingleChoiceItem) {
                if (item.checked) {
                    return index
                }
            }
        }
        return -1
    }

    fun callback(callback: SingleChoiceItemsCallback<String>) {
        this.callback = callback
    }

    override fun items(items: List<*>) {
        delegate.items(items.map { SingleChoiceItem(it.toString(), false) })
    }

}

private class SingleChoiceItem(var data: String, var checked: Boolean)
