@file:Suppress("unused", "CanBeParameter", "SetTextI18n")

package com.tuuzed.androidx.dialog.ex.date

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ex.basic.CustomViewNamespace
import com.tuuzed.androidx.dialog.ex.basic.DialogButtonClick
import com.tuuzed.androidx.dialog.ex.basic.DialogNamespaceInterface
import com.tuuzed.androidx.dialog.ex.basic.customView
import java.util.*

@SuppressLint("InflateParams")
fun ExDialog.dateRange(func: DateRangeNamespace.() -> Unit) {
    customView {
        val inflater = LayoutInflater.from(windowContext)
        val view = inflater.inflate(R.layout.part_dialog_daterange, null, false)
        val namespace = DateRangeNamespace(this@dateRange, this, view)
        func(namespace)
        customView(view)
    }
}

class DateRangeNamespace(
    private val dialog: ExDialog,
    private val delegate: CustomViewNamespace,
    private val view: View
) : DialogNamespaceInterface by delegate {

    private val beginText: TextView = view.findViewById(R.id.beginText)
    private val endText: TextView = view.findViewById(R.id.endText)
    private val datePicker: DatePicker = view.findViewById(R.id.datePicker)

    private var callback: DateRangeDialogCallback? = null
    private var dateChangedCallback: DateRangeDialogCallback? = null

    private var beginDate = Date()
    private var endDate = Date()

    private var selectedBegin = true

    init {
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

    fun beginDate(date: Date) {
        beginDate = datePicker.dateFormat.let { it.parse(it.format(date)) }
    }

    fun endDate(date: Date) {
        endDate = datePicker.dateFormat.let { it.parse(it.format(date)) }
    }

    fun maxYear(maxYear: Int) {
        datePicker.setMaxYear(maxYear)
        datePicker.type
    }

    fun minYear(minYear: Int) {
        datePicker.setMinYear(minYear)
    }

    fun type(@DatePickerType type: Int) {
        datePicker.type = type
    }

    fun onDateChanged(callback: DateRangeDialogCallback) {
        this.dateChangedCallback = callback
    }

    fun callback(callback: DateRangeDialogCallback) {
        this.callback = callback
    }

    override fun positiveButton(
        text: CharSequence,
        color: Int?,
        icon: Drawable?,
        click: DialogButtonClick?
    ) {
        delegate.positiveButton(text, color, icon) { dialog, which ->
            callback?.invoke(
                datePicker.dateFormat.let { it.parse(it.format(beginDate)) },
                datePicker.dateFormat.let { it.parse(it.format(endDate)) }
            )
            click?.invoke(dialog, which)
        }
    }

}