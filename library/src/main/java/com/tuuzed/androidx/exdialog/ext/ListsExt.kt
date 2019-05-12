@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.exdialog.ext

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.google.android.material.button.MaterialButton
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ListsControllerInterface
import com.tuuzed.androidx.exdialog.internal.MaterialButtonCompat
import com.tuuzed.recyclerview.adapter.RecyclerViewAdapter

inline fun ExDialog.lists(func: ListsController.() -> Unit) {
    customView {
        func(ListsController(this@lists, this) { customView(it) })
    }
}

class ListsController(
    private val dialog: ExDialog,
    private val delegate: CustomViewController,
    attachView: (View) -> Unit
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface {

    private val loadingIcon: SpinKitView
    private val button: MaterialButton
    private val recyclerView: RecyclerView
    private val listAdapter: RecyclerViewAdapter

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.part_dialog_lists, null, false)
        loadingIcon = view.findViewById(R.id.loadingIcon)
        button = view.findViewById(R.id.button)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(dialog.windowContext)
        listAdapter = RecyclerViewAdapter.with(recyclerView)
        showLoadingView()
        attachView(view)
    }

    fun config(func: (RecyclerView, RecyclerViewAdapter) -> Unit) {
        func(recyclerView, listAdapter)
    }

    fun items(items: List<*>, showItemView: Boolean = true) {
        listAdapter.items.clear()
        listAdapter.appendItems(items).notifyDataSetChanged()
        if (showItemView) {
            showItemsView()
        }
    }

    override fun showLoadingView(icon: Sprite?, @ColorInt color: Int?) {
        color?.also { loadingIcon.setColor(it) }
        icon?.also { loadingIcon.setIndeterminateDrawable(it) }

        loadingIcon.visibility = View.VISIBLE
        button.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    override fun showMessageView(text: CharSequence, @ColorInt color: Int?, click: () -> Unit) {
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

    override fun showItemsView() {
        loadingIcon.visibility = View.GONE
        button.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }


}
