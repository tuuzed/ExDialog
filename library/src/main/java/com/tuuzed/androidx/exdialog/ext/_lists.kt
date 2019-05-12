package com.tuuzed.androidx.exdialog.ext

import com.tuuzed.androidx.exdialog.R
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder

internal typealias Space = Unit

internal val SpaceItemViewBinder = object : AbstractItemViewBinder<Space>() {
    override fun getLayoutId(): Int = R.layout.listitem_space
    override fun onBindViewHolder(holder: CommonItemViewHolder, item: Space, position: Int) {}
}

typealias ItemToReadable<T> = (data: T) -> String