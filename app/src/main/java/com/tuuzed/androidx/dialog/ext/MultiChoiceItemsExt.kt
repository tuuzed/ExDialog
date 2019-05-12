@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import android.widget.CheckBox
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ListsControllerInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder

inline fun <T> ExDialog.Factory.showMultiChoiceItems(
    windowContext: Context,
    func: MultiChoiceItemsController<T>.() -> Unit
) {
    ExDialog.show(windowContext) { multiChoiceItems(func) }
}


inline fun <T> ExDialog.multiChoiceItems(func: MultiChoiceItemsController<T>.() -> Unit) {
    lists {
        func(MultiChoiceItemsController(this@multiChoiceItems, this))
    }
}

class MultiChoiceItemsController<T>(
    private val dialog: ExDialog,
    private val delegate: ListsController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var callback: MultiChoiceItemsCallback<T>? = null

    init {
        delegate.config { _, listAdapter ->
            listAdapter.bind(MultiChoiceItem::class.java, object : AbstractItemViewBinder<MultiChoiceItem<T>>() {
                override fun getLayoutId(): Int = R.layout.listitem_multichoiceitems
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: MultiChoiceItem<T>, position: Int) {
                    holder.text(R.id.text, item.data.toString())
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

    fun items(items: List<T>) {
        delegate.items(items.map { MultiChoiceItem(it, false) })
    }

}

private class MultiChoiceItem<T>(val data: T, var checked: Boolean)