@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

@SuppressLint("InflateParams")
inline fun ExDialog.lists(func: ListsConfigurator.() -> Unit) {
    customView {
        val inflater = LayoutInflater.from(windowContext)
        val view = inflater.inflate(R.layout.part_dialog_lists, null, false)
        val configurator = ListsConfigurator(this@lists, this, view)
        func(configurator)
        customView(view)
    }
}


class ListsConfigurator(
    private val dialog: ExDialog,
    private val configurator: CustomViewConfigurator,
    private val view: View
) : DialogConfiguratorInterface by configurator {

    private val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

    init {
        recyclerView.layoutManager = LinearLayoutManager(dialog.windowContext)
    }

    fun layoutManager(layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    fun <VH : RecyclerView.ViewHolder> adapter(adapter: RecyclerView.Adapter<VH>) {
        recyclerView.adapter = adapter
    }


}
