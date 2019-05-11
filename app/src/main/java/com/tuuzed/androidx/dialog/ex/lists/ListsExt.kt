@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.dialog.ex.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.google.android.material.button.MaterialButton
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ex.basic.CustomViewNamespace
import com.tuuzed.androidx.dialog.ex.basic.DialogNamespaceInterface
import com.tuuzed.androidx.dialog.ex.basic.customView
import com.tuuzed.androidx.dialog.internal.MaterialButtonCompat
import com.tuuzed.recyclerview.adapter.RecyclerViewAdapter

@SuppressLint("InflateParams")
inline fun ExDialog.lists(func: ListsNamespace.() -> Unit) {
    customView {
        val inflater = LayoutInflater.from(windowContext)
        val view = inflater.inflate(R.layout.part_dialog_lists, null, false)
        val configurator = ListsNamespace(this@lists, this, view)
        func(configurator)
        customView(view)
    }
}

class ListsNamespace(
    private val dialog: ExDialog,
    private val delegate: CustomViewNamespace,
    private val view: View
) : DialogNamespaceInterface by delegate, ListsNamespaceInterface {

    private val loadingIcon: SpinKitView = view.findViewById(R.id.loadingIcon)
    private val button: MaterialButton = view.findViewById(R.id.button)
    private val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

    private val listAdapter = RecyclerViewAdapter.with(recyclerView)

    init {
        recyclerView.layoutManager = LinearLayoutManager(dialog.windowContext)
        showLoadingView()
    }

    fun config(func: (RecyclerView, RecyclerViewAdapter) -> Unit) {
        func(recyclerView, listAdapter)
    }

    override fun showLoadingView(icon: Sprite?, @ColorInt color: Int?) {
        color?.also { loadingIcon.setColor(it) }
        icon?.also { loadingIcon.setIndeterminateDrawable(it) }

        loadingIcon.visibility = View.VISIBLE
        button.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    override fun showClickButton(text: CharSequence, @ColorInt color: Int?, click: () -> Unit) {
        button.text = text
        button.setOnClickListener { click() }

        color?.also {
            button.setTextColor(color)
            MaterialButtonCompat.setRippleColor(button, it)
        }
        loadingIcon.visibility = View.GONE
        button.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun items(items: List<*>) {
        listAdapter.items.clear()
        listAdapter.appendItems(items).notifyDataSetChanged()

        loadingIcon.visibility = View.GONE
        button.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }


}
