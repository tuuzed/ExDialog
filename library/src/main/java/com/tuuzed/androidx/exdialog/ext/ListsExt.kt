package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.DialogButton
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ListsControllerInterface

fun ExDialog.lists(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    func: ListsController.() -> Unit
) {
    customView(titleRes, title, iconRes, icon) {
        ListsController(this@lists, this) {
            customView(it)
        }.apply(func)
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
    private val button: DialogButton
    private val recyclerView: RecyclerView
    private val listAdapter: DialogListsAdapter

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.part_dialog_lists, null, false)
        loadingIcon = view.findViewById(R.id.loadingIcon)
        button = view.findViewById(R.id.button)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(dialog.windowContext)
        listAdapter = DialogListsAdapter()
        recyclerView.adapter = listAdapter
        showLoadingView()
        attachView(view)
    }

    fun config(func: (RecyclerView, DialogListsAdapter) -> Unit) {
        func(recyclerView, listAdapter)
    }

    fun items(items: List<*>, showItemView: Boolean = true) {
        listAdapter.items.clear()
        @Suppress("UNCHECKED_CAST")
        listAdapter.items.addAll(items as List<Any>)
        listAdapter.notifyDataSetChanged()
        if (showItemView) {
            showItemsView()
        }
    }

    override fun showLoadingView(
        icon: Sprite?,
        @ColorRes colorRes: Int?, @ColorInt color: Int?
    ) {
        icon?.also { loadingIcon.setIndeterminateDrawable(it) }

        colorRes?.also { loadingIcon.setColor(dialog.context.resColor(it)) }
        color?.also { loadingIcon.setColor(it) }

        loadingIcon.visibility = View.VISIBLE
        button.visibility = View.GONE
        recyclerView.visibility = View.GONE

    }

    override fun showMessageView(
        @StringRes textRes: Int?, text: CharSequence?,
        @ColorRes textColorRes: Int?, @ColorInt textColor: Int?,
        click: () -> Unit
    ) {
        textRes?.also { button.setText(it) }
        text?.also { button.text = it }

        textColorRes?.also { button.setButtonColor(dialog.context.resColor(it)) }
        textColor?.also { button.setButtonColor(it) }

        button.setOnClickListener { click() }

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




