@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

fun ExDialog.items(func: ItemsConfigurator.() -> Unit) {
    lists {
        layoutManager(LinearLayoutManager(this@items.windowContext))
        val configurator = ItemsConfigurator(this@items, this)
        func(configurator)
    }
}


class ItemsConfigurator(
    private val dialog: ExDialog,
    private val configurator: ListsConfigurator
) : DialogConfiguratorInterface by configurator {

    private var callback: ItemsDialogCallback? = null

    fun callback(callback: ItemsDialogCallback) {
        this.callback = callback
    }

    fun items(vararg items: CharSequence) {
        items(listOf(*items))
    }

    fun items(items: List<CharSequence>) {
        configurator.adapter(ItemsAdapter(items) { item, position ->
            callback?.invoke(dialog, item, position)
        })
    }

}


private class ItemsAdapter(
    private val items: List<CharSequence>,
    private val callback: (CharSequence, Int) -> Unit
) : RecyclerView.Adapter<ItemsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.listitem_items, parent, false)
        return ItemsViewHolder(itemView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.itemText.text = items[position]
        holder.itemView.setOnClickListener { callback(items[position], position) }
    }
}


private class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemText: TextView = itemView.findViewById(R.id.text)
}