@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ex.lists

import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ex.basic.DialogNamespaceInterface
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder

fun ExDialog.simpleItems(func: ItemsNamespace.() -> Unit) {
    lists {
        val namespace = ItemsNamespace(this@simpleItems, this)
        func(namespace)
    }
}

class ItemsNamespace(
    private val dialog: ExDialog,
    private val delegate: ListsNamespace
) : DialogNamespaceInterface by delegate, ListsNamespaceInterface by delegate {

    private var callback: ItemsDialogCallback<String>? = null

    init {
        delegate.config { _, listAdapter ->
            listAdapter.bind(Space::class.java, object : AbstractItemViewBinder<Space>() {
                override fun getLayoutId(): Int = R.layout.listitem_space
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: Space, position: Int) {}
            })
            listAdapter.bind(String::class.java, object : AbstractItemViewBinder<String>() {
                override fun getLayoutId(): Int = R.layout.listitem_items
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: String, position: Int) {
                    holder.text(R.id.text, item)
                    holder.click(R.id.text) { callback?.invoke(dialog, item, position) }
                }
            })
        }
    }

    fun callback(callback: ItemsDialogCallback<String>) {
        this.callback = callback
    }
}

private class Space
