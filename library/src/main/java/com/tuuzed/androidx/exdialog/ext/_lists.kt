package com.tuuzed.androidx.exdialog.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tuuzed.androidx.exdialog.R

internal typealias Space = Unit

internal val SpaceItemViewBinder = object : DialogListsAdapter.Binder<Space> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogListsAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.listitem_space, parent, false
        )
        return DialogListsAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DialogListsAdapter.ViewHolder, position: Int, item: Space) {}
}

typealias ItemToReadable<T> = (data: T) -> String


