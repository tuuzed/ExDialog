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

inline fun ExDialog.Factory.multiChoiceItems(windowContext: Context, func: MultiChoiceItemsController.() -> Unit) {
    ExDialog.show(windowContext) { multiChoiceItems(func) }
}


inline fun ExDialog.multiChoiceItems(func: MultiChoiceItemsController.() -> Unit) {
    lists {
        func(MultiChoiceItemsController(this@multiChoiceItems, this))
    }
}

class MultiChoiceItemsController(
    private val dialog: ExDialog,
    private val delegate: ListsController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var callback: MultiChoiceItemsCallback<String>? = null

    init {
        delegate.config { _, listAdapter ->
            listAdapter.bind(MultiChoiceItem::class.java, object : AbstractItemViewBinder<MultiChoiceItem>() {
                override fun getLayoutId(): Int = R.layout.listitem_multichoiceitems
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: MultiChoiceItem, position: Int) {
                    holder.text(R.id.text, item.data)
                    holder.find<CheckBox>(R.id.checkbox).isChecked = item.checked
                    holder.click(R.id.item_layout) {
                        item.checked = !item.checked
                        listAdapter.notifyItemChanged(position)
                    }
                }
            })
        }
    }

    fun callback(callback: MultiChoiceItemsCallback<String>) {
        this.callback = callback
    }

    override fun items(items: List<*>) {
        delegate.items(items.map { MultiChoiceItem(it.toString(), false) })
    }

}

private class MultiChoiceItem(var data: String, var checked: Boolean)