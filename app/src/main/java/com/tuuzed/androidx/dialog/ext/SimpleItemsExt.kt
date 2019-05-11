@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ListsControllerInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder
import java.util.*

inline fun ExDialog.Factory.simpleItems(windowContext: Context, func: SimpleItemsController.() -> Unit) {
    ExDialog.show(windowContext) { simpleItems(func) }
}

inline fun ExDialog.simpleItems(func: SimpleItemsController.() -> Unit) {
    lists {
        func(SimpleItemsController(this@simpleItems, this))
    }
}

class SimpleItemsController(
    private val dialog: ExDialog,
    private val delegate: ListsController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate {

    private var callback: ItemsCallback<String>? = null

    init {
        delegate.config { _, listAdapter ->
            listAdapter.bind(String::class.java, object : AbstractItemViewBinder<String>() {
                override fun getLayoutId(): Int = R.layout.listitem_items
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: String, position: Int) {
                    holder.text(R.id.text, item)
                    holder.click(R.id.text) { callback?.invoke(dialog, item, position) }
                }
            })
        }
    }

    fun callback(callback: ItemsCallback<String>) {
        this.callback = callback
    }

}

