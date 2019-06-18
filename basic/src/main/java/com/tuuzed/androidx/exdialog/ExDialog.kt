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
import com.tuuzed.androidx.exdialog.internal.adjustButtonColor
import com.tuuzed.androidx.exdialog.internal.indices
import com.tuuzed.androidx.exdialog.internal.values
import java.util.concurrent.CopyOnWriteArrayList

class ExDialog constructor(
    val context: Context
) : DialogInterface {

    companion object {
        const val ON_PRE_SHOW = 1
        const val ON_SHOW = 2
        const val ON_CANCEL = 3
        const val ON_DISMISS = 4
        const val ON_CLICK_POSITIVE_BUTTON = 5
        const val ON_CLICK_NEGATIVE_BUTTON = 6
        const val ON_CLICK_NEUTRAL_BUTTON = 7

        private const val CONTENT_VIEW_NONE = 0
        // 消息
        private const val CONTENT_VIEW_MESSAGE = 1
        // 列表
        private const val CONTENT_VIEW_ITEMS = 2
        // 单选列表
        private const val CONTENT_VIEW_SINGLE_CHOICE_ITEMS = 3
        // 多选列表
        private const val CONTENT_VIEW_MULTI_CHOICE_ITEMS = 4
        // 自定义View
        private const val CONTENT_VIEW_CUSTOM_VIEW = 5
    }

    private var alertDialog: AlertDialog? = null
    private val eventWatcherList = CopyOnWriteArrayList<ExDialogEventWatcher>()

    private var cancelable: Boolean? = null
    private var canceledOnTouchOutside: Boolean? = null

    private var icon: Drawable? = null
    private var title: CharSequence? = null
    //
    private var contentViewIdentifier = CONTENT_VIEW_NONE
    // 自定义View类型
    var customViewType = 0
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
                eventWatcherList.forEach { it.invoke(this, ON_CLICK_NEUTRAL_BUTTON) }
                neutralButtonCallback?.invoke(this)
            }
            .setNegativeButtonIcon(negativeButtonIcon)
            .setNegativeButton(negativeButtonText) { _, _ ->
                eventWatcherList.forEach { it.invoke(this, ON_CLICK_NEGATIVE_BUTTON) }
                negativeButtonCallback?.invoke(this)
            }
            .setPositiveButtonIcon(positiveButtonIcon)
            .setPositiveButton(positiveButtonText) { _, _ ->
                when (contentViewIdentifier) {
                    // 单选列表
                    CONTENT_VIEW_SINGLE_CHOICE_ITEMS -> singleChoiceItemsCallback?.invoke(
                        this,
                        singleChoiceCheckedIndex
                    )
                    // 多选列表
                    CONTENT_VIEW_MULTI_CHOICE_ITEMS -> multiChoiceItemsCallback?.invoke(
                        this,
                        multiChoiceCheckedIndices.indices(true)
                    )
                }
                eventWatcherList.forEach { it.invoke(this, ON_CLICK_POSITIVE_BUTTON) }
                positiveButtonCallback?.invoke(this)
            }
            .setOnCancelListener {
                eventWatcherList.forEach { it.invoke(this, ON_CANCEL) }
            }
            .setOnDismissListener {
                eventWatcherList.forEach { it.invoke(this, ON_DISMISS) }
                eventWatcherList.clear()
            }
        when (contentViewIdentifier) {
            // 消息
            CONTENT_VIEW_MESSAGE -> builder.setMessage(message)
            // 列表
            CONTENT_VIEW_ITEMS -> builder.setItems(items) { _, which ->
                itemsCallback?.invoke(this, which)
            }
            // 单选列表
            CONTENT_VIEW_SINGLE_CHOICE_ITEMS -> builder.setSingleChoiceItems(
                items, singleChoiceCheckedIndex
            ) { _, which ->
                singleChoiceCheckedIndex = which
                singleChoiceItemsWatcher?.invoke(this, which)
            }
            // 多选列表
            CONTENT_VIEW_MULTI_CHOICE_ITEMS -> builder.setMultiChoiceItems(
                items, multiChoiceCheckedIndices.values()
            ) { _, which, isChecked ->
                multiChoiceCheckedIndices.put(which, isChecked)
                multiChoiceItemsWatcher?.invoke(this, which, isChecked)
            }
            // 自定义View
            CONTENT_VIEW_CUSTOM_VIEW -> if (customView != null) {
                builder.setView(customView)
            } else if (customViewLayoutRes != View.NO_ID) {
                builder.setView(customViewLayoutRes)
            }
        }
        builder.create().apply {
            alertDialog = this
            cancelable?.let { setCancelable(it) }
            canceledOnTouchOutside?.let { setCanceledOnTouchOutside(it) }
            eventWatcherList.forEach { it.invoke(this@ExDialog, ON_PRE_SHOW) }
            show()
            // 设置按钮颜色
            neutralButton = getButton(DialogInterface.BUTTON_NEUTRAL)
            negativeButton = getButton(DialogInterface.BUTTON_NEGATIVE)
            positiveButton = getButton(DialogInterface.BUTTON_POSITIVE)
            adjustButtonColor(neutralButton, neutralButtonColor)
            adjustButtonColor(negativeButton, negativeButtonColor)
            adjustButtonColor(positiveButton, positiveButtonColor)
            eventWatcherList.forEach { it.invoke(this@ExDialog, ON_SHOW) }
        }
        return this
    }

    override fun cancel() {
        alertDialog?.cancel()
    }

    override fun dismiss() {
        alertDialog?.dismiss()
    }

    val window: Window? get() = alertDialog?.window

    fun cancelable(flag: Boolean) {
        this.cancelable = flag
    }

    fun canceledOnTouchOutside(cancel: Boolean) {
        this.canceledOnTouchOutside = cancel
    }

    fun icon(@DrawableRes iconRes: Int = View.NO_ID, icon: Drawable? = null) {
        this.icon = com.tuuzed.androidx.exdialog.internal.resolveDrawable(context, iconRes, icon)
    }

    fun title(@StringRes textRes: Int = View.NO_ID, text: CharSequence? = null) {
        this.title = com.tuuzed.androidx.exdialog.internal.resolveString(context, textRes, text)
    }

    fun message(@StringRes textRes: Int = View.NO_ID, text: CharSequence? = null) {
        this.message = com.tuuzed.androidx.exdialog.internal.resolveString(context, textRes, text)
        this.contentViewIdentifier = CONTENT_VIEW_MESSAGE
    }

    fun items(items: Array<out CharSequence>, callback: ItemsCallback = null) {
        this.items = items
        this.itemsCallback = callback
        this.contentViewIdentifier = CONTENT_VIEW_ITEMS
    }

    fun singleChoiceItems(
        items: Array<out CharSequence>,
        checkedIndex: Int = -1,
        watcher: SingleChoiceItemsWatcher = null,
        callback: SingleChoiceItemsCallback = null
    ) {
        this.items = items
        this.singleChoiceCheckedIndex = checkedIndex
        this.singleChoiceItemsWatcher = watcher
        this.singleChoiceItemsCallback = callback
        this.contentViewIdentifier = CONTENT_VIEW_SINGLE_CHOICE_ITEMS
    }

    fun multiChoiceItems(
        items: Array<out CharSequence>,
        checkedIndices: IntArray = IntArray(0),
        watcher: MultiChoiceItemsWatcher = null,
        callback: MultiChoiceItemsCallback = null
    ) {
        this.items = items
        this.multiChoiceCheckedIndices = SparseBooleanArray(items.size)
        for (i in 0 until items.size) {
            multiChoiceCheckedIndices.put(i, checkedIndices.contains(i))
        }
        this.multiChoiceItemsWatcher = watcher
        this.multiChoiceItemsCallback = callback
        this.contentViewIdentifier = CONTENT_VIEW_MULTI_CHOICE_ITEMS
    }


    fun customView(@LayoutRes layoutRes: Int = View.NO_ID, view: View? = null) {
        this.customViewLayoutRes = layoutRes
        this.customView = view
        this.contentViewIdentifier = CONTENT_VIEW_CUSTOM_VIEW
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
        this.neutralButtonIcon = com.tuuzed.androidx.exdialog.internal.resolveDrawable(context, iconRes, icon)
        this.neutralButtonText = com.tuuzed.androidx.exdialog.internal.resolveString(context, textRes, text)
        this.neutralButtonColor = com.tuuzed.androidx.exdialog.internal.resolveColor(context, colorRes, color)
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
        this.negativeButtonIcon = com.tuuzed.androidx.exdialog.internal.resolveDrawable(context, iconRes, icon)
        this.negativeButtonText = com.tuuzed.androidx.exdialog.internal.resolveString(context, textRes, text)
        this.negativeButtonColor = com.tuuzed.androidx.exdialog.internal.resolveColor(context, colorRes, color)
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
        this.positiveButtonIcon = com.tuuzed.androidx.exdialog.internal.resolveDrawable(context, iconRes, icon)
        this.positiveButtonText = com.tuuzed.androidx.exdialog.internal.resolveString(context, textRes, text)
        this.positiveButtonColor = com.tuuzed.androidx.exdialog.internal.resolveColor(context, colorRes, color)
        this.positiveButtonCallback = callback
    }


    fun addEventWatcher(watcher: ExDialogEventWatcher) {
        eventWatcherList.add(watcher)
    }

    fun removeEventWatcher(watcher: ExDialogEventWatcher) {
        eventWatcherList.remove(watcher)
    }

    private fun createAlertDialogBuilder(material: Boolean) = if (material) try {
        MaterialAlertDialogBuilder(context)
    } catch (e: Exception) {
        AlertDialog.Builder(context)
    } else {
        AlertDialog.Builder(context)
    }
}