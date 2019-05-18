package com.tuuzed.androidx.exdialog.ext

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

class DialogListsAdapter : RecyclerView.Adapter<DialogListsAdapter.ViewHolder>() {
    private val types = mutableListOf<Class<*>>()
    private val binders = mutableListOf<Binder<*>>()

    val items = mutableListOf<Any>()

    fun <T> bind(type: Class<in T>, factory: Binder<in T>) {
        types.add(type)
        binders.add(factory)
    }

    override fun getItemViewType(position: Int): Int {
        val type = items[position]::class.java
        val typeIndex = types.indexOf(type)
        if (typeIndex == -1) {
            throw IllegalStateException("DialogListsAdapter: unbind type [$type].")
        } else {
            return typeIndex
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binder = binders[viewType]
        return binder.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        @Suppress("UNCHECKED_CAST")
        val binder = binders[viewType] as Binder<in Any>
        val item = items[position]
        binder.onBindViewHolder(holder, position, item)
    }

    interface Binder<T> {
        fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        fun onBindViewHolder(holder: ViewHolder, position: Int, item: T)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cacheViews = SparseArray<View>()

        fun <T : View> find(@IdRes id: Int): T {
            val cacheView = cacheViews.get(id)
            if (cacheView != null) {
                @Suppress("UNCHECKED_CAST")
                return cacheView as T
            } else {
                val view = itemView.findViewById<T>(id)
                cacheViews.put(id, view)
                return view
            }
        }

    }

}
