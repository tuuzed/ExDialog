package com.tuuzed.androidx.exdialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.util.SparseBooleanArray
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.ref.WeakReference
import java.util.concurrent.CopyOnWriteArrayList

class ExDialog constructor(
    val context: Context
) : DialogInterface {

    companion object {
        const val FLAG_NONE = 0
        // 消息
        const val FLAG_MESSAGE = 1
        // 列表
        const val FLAG_ITEMS = 2
        // 单选列表
        const val FLAG_SINGLE_CHOICE_ITEMS = 3
        // 多选列表
        const val FLAG_MULTI_CHOICE_ITEMS = 4
        // 自定义View
        const val FLAG_CUSTOM_VIEW = 5
    }

    private var alertDialog: AlertDialog? = null
    private val eventWatchers = CopyOnWriteArrayList<WeakReference<ExDialogEventWatcher>>()
    val window: Window? get() = alertDialog?.window

    private var cancelable: Boolean? = null
    private var canceledOnTouchOutside: Boolean? = null

    private var icon: Drawable? = null
    private var title: CharSequence? = null
    //
    private var mutexViewFlag = FLAG_NONE
    var customViewFlag = -1
    //
    private var message: CharSequence? = null
    // 列表
    private var items: Array<out CharSequence>? = null
    private var itemsCallback: ItemsCallback = null
    private var singleChoiceItemsWatcher: SingleChoiceItemsWatcher = null
    private var singleChoiceItemsCallback: SingleChoiceItemsCallback = null
    private var singleChoiceCheckedIndex: Int = -1
    private var multiChoiceItemsWatcher: MultiChoiceItemsWatcher = null
    private var multiChoiceItemsCallback: MultiChoiceItemsCallback = null
    private var multiChoiceCheckedIndices: SparseBooleanArray = SparseBooleanArray(0)


    // CustomView
    private var customViewLayoutRes: Int = View.NO_ID
    private var customView: View? = null

    // neutralButton
    private var neutralButton: Button? = null
    private var neutralButtonIcon: Drawable? = null
    private var neutralButtonText: CharSequence? = null
    private var neutralButtonColor: Int? = null
    private var neutralButtonCallback: ExDialogCallback = null

    // negativeButton
    private var negativeButton: Button? = null
    private var negativeButtonIcon: Drawable? = null
    private var negativeButtonText: CharSequence? = null
    private var negativeButtonColor: Int? = null
    private var negativeButtonCallback: ExDialogCallback = null

    // positiveButton
    private var positiveButton: Button? = null
    private var positiveButtonIcon: Drawable? = null
    private var positiveButtonText: CharSequence? = null
    private var positiveButtonColor: Int? = null
    private var positiveButtonCallback: ExDialogCallback = null

    fun show(material: Boolean = true, func: ExDialog.() -> Unit): ExDialog {
        func(this)
        val builder = createAlertDialogBuilder(material).setIcon(icon)
            .setTitle(title)
            .setNeutralButtonIcon(neutralButtonIcon)
            .setNeutralButton(neutralButtonText) { _, _ ->
                eventWatchers.forEach { it.get()?.invoke(this, ExDialogEvent.ON_CLICK_NEUTRAL_BUTTON) }
                neutralButtonCallback?.invoke(this)
            }
            .setNegativeButtonIcon(negativeButtonIcon)
            .setNegativeButton(negativeButtonText) { _, _ ->
                eventWatchers.forEach { it.get()?.invoke(this, ExDialogEvent.ON_CLICK_NEGATIVE_BUTTON) }
                negativeButtonCallback?.invoke(this)
            }
            .setPositiveButtonIcon(positiveButtonIcon)
            .setPositiveButton(positiveButtonText) { _, _ ->
                when (mutexViewFlag) {
                    // 单选列表
                    FLAG_SINGLE_CHOICE_ITEMS -> singleChoiceItemsCallback?.invoke(
                        this,
                        singleChoiceCheckedIndex
                    )
                    // 多选列表
                    FLAG_MULTI_CHOICE_ITEMS -> multiChoiceItemsCallback?.invoke(
                        this,
                        multiChoiceCheckedIndices.indices(true)
                    )
                }
                eventWatchers.forEach { it.get()?.invoke(this, ExDialogEvent.ON_CLICK_POSITIVE_BUTTON) }
                positiveButtonCallback?.invoke(this)
            }
            .setOnCancelListener { eventWatchers.forEach { it.get()?.invoke(this, ExDialogEvent.ON_CANCEL) } }
            .setOnDismissListener { eventWatchers.forEach { it.get()?.invoke(this, ExDialogEvent.ON_DISMISS) } }
        when (mutexViewFlag) {
            // 消息
            FLAG_MESSAGE -> builder.setMessage(message)
            // 列表
            FLAG_ITEMS -> builder.setItems(items) { _, which ->
                itemsCallback?.invoke(this, which)
            }
            // 单选列表
            FLAG_SINGLE_CHOICE_ITEMS -> builder.setSingleChoiceItems(
                items, singleChoiceCheckedIndex
            ) { _, which ->
                singleChoiceCheckedIndex = which
                singleChoiceItemsWatcher?.invoke(this, which)
            }
            // 多选列表
            FLAG_MULTI_CHOICE_ITEMS -> builder.setMultiChoiceItems(
                items, multiChoiceCheckedIndices.values()
            ) { _, which, isChecked ->
                multiChoiceCheckedIndices.put(which, isChecked)
                multiChoiceItemsWatcher?.invoke(this, which, isChecked)
            }
            // 自定义View
            FLAG_CUSTOM_VIEW -> if (customView != null) {
                builder.setView(customView)
            } else if (customViewLayoutRes != View.NO_ID) {
                builder.setView(customViewLayoutRes)
            }
        }
        builder.create().apply {
            alertDialog = this
            cancelable?.let { setCancelable(it) }
            canceledOnTouchOutside?.let { setCanceledOnTouchOutside(it) }
            eventWatchers.forEach { it.get()?.invoke(this@ExDialog, ExDialogEvent.ON_PRE_SHOW) }
            show()
            // 设置按钮颜色
            neutralButton = getButton(DialogInterface.BUTTON_NEUTRAL)
            negativeButton = getButton(DialogInterface.BUTTON_NEGATIVE)
            positiveButton = getButton(DialogInterface.BUTTON_POSITIVE)
            adjustButtonColor(neutralButton, neutralButtonColor)
            adjustButtonColor(negativeButton, negativeButtonColor)
            adjustButtonColor(positiveButton, positiveButtonColor)
            eventWatchers.forEach { it.get()?.invoke(this@ExDialog, ExDialogEvent.ON_SHOW) }
        }
        return this
    }

    override fun cancel() {
        alertDialog?.cancel()
    }

    override fun dismiss() {
        alertDialog?.dismiss()
        eventWatchers.clear()
    }

    fun cancelable(flag: Boolean) {
        this.cancelable = flag
    }

    fun canceledOnTouchOutside(cancel: Boolean) {
        this.canceledOnTouchOutside = cancel
    }

    fun icon(@DrawableRes iconRes: Int = View.NO_ID, icon: Drawable? = null) {
        this.icon = resolveDrawable(context, iconRes, icon)
    }

    fun title(@StringRes textRes: Int = View.NO_ID, text: CharSequence? = null) {
        this.title = resolveString(context, textRes, text)
    }

    fun message(@StringRes textRes: Int = View.NO_ID, text: CharSequence? = null) {
        this.message = resolveString(context, textRes, text)
        this.mutexViewFlag = FLAG_MESSAGE
    }

    fun items(items: Array<out CharSequence>, callback: ItemsCallback) {
        this.items = items
        this.itemsCallback = callback
        this.mutexViewFlag = FLAG_ITEMS
    }

    fun singleChoiceItems(
        items: Array<out CharSequence>,
        checkedIndex: Int = -1,
        watcher: SingleChoiceItemsWatcher,
        callback: SingleChoiceItemsCallback
    ) {
        this.items = items
        this.singleChoiceCheckedIndex = checkedIndex
        this.singleChoiceItemsWatcher = watcher
        this.singleChoiceItemsCallback = callback
        this.mutexViewFlag = FLAG_SINGLE_CHOICE_ITEMS
    }

    fun multiChoiceItems(
        items: Array<out CharSequence>,
        checkedIndices: IntArray = IntArray(0),
        watcher: MultiChoiceItemsWatcher,
        callback: MultiChoiceItemsCallback
    ) {
        this.items = items
        this.multiChoiceCheckedIndices = SparseBooleanArray(items.size)
        for (i in 0 until items.size) {
            multiChoiceCheckedIndices.put(i, checkedIndices.contains(i))
        }
        this.multiChoiceItemsWatcher = watcher
        this.multiChoiceItemsCallback = callback
        this.mutexViewFlag = FLAG_MULTI_CHOICE_ITEMS
    }


    fun customView(@LayoutRes layoutRes: Int = View.NO_ID, view: View? = null) {
        this.customViewLayoutRes = layoutRes
        this.customView = view
        this.mutexViewFlag = FLAG_CUSTOM_VIEW
    }

    fun setNeutralButtonEnable(enable: Boolean) {
        neutralButton?.isEnabled = enable
    }

    fun neutralButton(
        @DrawableRes iconRes: Int = View.NO_ID, icon: Drawable? = null,
        @StringRes textRes: Int = View.NO_ID, text: CharSequence? = null,
        @ColorRes colorRes: Int = View.NO_ID, @ColorInt color: Int = -1,
        callback: ExDialogCallback = null
    ) {
        this.neutralButtonIcon = resolveDrawable(context, iconRes, icon)
        this.neutralButtonText = resolveString(context, textRes, text)
        this.neutralButtonColor = resolveColor(context, colorRes, color)
        this.neutralButtonCallback = callback
    }

    fun setNegativeButtonEnable(enable: Boolean) {
        negativeButton?.isEnabled = enable
    }

    fun negativeButton(
        @DrawableRes iconRes: Int = View.NO_ID, icon: Drawable? = null,
        @StringRes textRes: Int = android.R.string.cancel, text: CharSequence? = null,
        @ColorRes colorRes: Int = View.NO_ID, @ColorInt color: Int = -1,
        callback: ExDialogCallback = null
    ) {
        this.negativeButtonIcon = resolveDrawable(context, iconRes, icon)
        this.negativeButtonText = resolveString(context, textRes, text)
        this.negativeButtonColor = resolveColor(context, colorRes, color)
        this.negativeButtonCallback = callback
    }

    fun setPositiveButtonEnable(enable: Boolean) {
        positiveButton?.isEnabled = enable
    }

    fun positiveButton(
        @DrawableRes iconRes: Int = View.NO_ID, icon: Drawable? = null,
        @StringRes textRes: Int = android.R.string.ok, text: CharSequence? = null,
        @ColorRes colorRes: Int = View.NO_ID, @ColorInt color: Int = -1,
        callback: ExDialogCallback = null
    ) {
        this.positiveButtonIcon = resolveDrawable(context, iconRes, icon)
        this.positiveButtonText = resolveString(context, textRes, text)
        this.positiveButtonColor = resolveColor(context, colorRes, color)
        this.positiveButtonCallback = callback
    }


    fun addEventWatcher(watcher: ExDialogEventWatcher) {
        eventWatchers.add(WeakReference(watcher))
    }

    fun removeEventWatcher(watcher: ExDialogEventWatcher) {
        var entry: WeakReference<ExDialogEventWatcher>? = null
        eventWatchers.forEach {
            if (it.get() == watcher) {
                entry = it
            }
        }
        eventWatchers.remove(entry)
    }

    private fun createAlertDialogBuilder(material: Boolean) = if (material) try {
        MaterialAlertDialogBuilder(context)
    } catch (e: Exception) {
        AlertDialog.Builder(context)
    } else {
        AlertDialog.Builder(context)
    }
}