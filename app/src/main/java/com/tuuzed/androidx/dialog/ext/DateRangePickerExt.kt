@file:Suppress("unused", "CanBeParameter", "SetTextI18n", "InflateParams")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import java.util.*

inline fun ExDialog.Factory.showDateRangePicker(windowContext: Context, func: DateRangeController.() -> Unit) {
    ExDialog.show(windowContext) { dateRangePicker(func) }
}

inline fun ExDialog.dateRangePicker(func: DateRangeController.() -> Unit) {
    customView {
        func(DateRangeController(this@dateRangePicker, this) { customView(it) })
    }
}

class DateRangeController(
    private val dialog: ExDialog,
    private val delegate: CustomViewController,
    attachView: (View) -> Unit
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate {

    private val beginText: TextView
    private val endText: TextView
    private val datePicker: DatePicker

    private var callback: DateRangeCallback? = null
    private var dateChangedCallback: DateRangeCallback? = null

    private var beginDate = Date()
    private var endDate = Date()

    private var selectedBegin = true

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.part_dialog_daterangepicker, null, false)

        beginText = view.findViewById(R.id.beginText)
        endText = view.findViewById(R.id.endText)
        datePicker = view.findViewById(R.id.datePicker)
        selectBegin()
        beginText.text = "开始于\n${datePicker.dateFormat.format(beginDate)}"
        endText.text = "结束于\n${datePicker.dateFormat.format(endDate)}"
        beginText.setOnClickListener { selectBegin() }
        endText.setOnClickListener { selectEnd() }
        datePicker.setOnDateChangedListener {
            if (selectedBegin) {
                beginDate = datePicker.date
                beginText.text = "开始于\n${datePicker.dateFormat.format(beginDate)}"
            } else {
                endDate = datePicker.date
                endText.text = "结束于\n${datePicker.dateFormat.format(endDate)}"
            }
            dateChangedCallback?.invoke(
                datePicker.dateFormat.let { it.parse(it.format(beginDate)) },
                datePicker.dateFormat.let { it.parse(it.format(endDate)) }
            )
        }
        attachView(view)
    }

    fun yearRange(max: Int = -1, min: Int = -1) {
        if (max > 0) {
            datePicker.setMaxYear(max)
        }
        if (min > 0) {
            datePicker.setMinYear(min)
        }
    }

    fun type(@DatePickerType type: Int) {
        datePicker.type = type
    }

    fun beginDate(date: Date) {
        beginDate = datePicker.dateFormat.let { it.parse(it.format(date)) }
    }

    fun endDate(date: Date) {
        endDate = datePicker.dateFormat.let { it.parse(it.format(date)) }
    }

    fun onDateChanged(callback: DateRangeCallback) {
        this.dateChangedCallback = callback
    }

    fun callback(callback: DateRangeCallback) {
        this.callback = callback
    }

    override fun positiveButton(text: CharSequence, @ColorInt color: Int?, icon: Drawable?, click: DialogButtonClick) {
        delegate.positiveButton(text, color, icon) { dialog, which ->
            callback?.invoke(
                datePicker.dateFormat.let { it.parse(it.format(beginDate)) },
                datePicker.dateFormat.let { it.parse(it.format(endDate)) }
            )
            click.invoke(dialog, which)
        }
    }

    private fun selectBegin() {
        selectedBegin = true
        beginText.setTextColor(0xFFD81B60.toInt())
        endText.setTextColor(0xFFBDBDBD.toInt())
        datePicker.date = beginDate
    }

    private fun selectEnd() {
        selectedBegin = false
        beginText.setTextColor(0xFFBDBDBD.toInt())
        endText.setTextColor(0xFFD81B60.toInt())
        datePicker.date = endDate
    }

}