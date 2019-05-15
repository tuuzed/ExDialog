@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter", "SetTextI18n", "InflateParams")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import java.util.*

fun ExDialog.dateRangePicker(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    @DatePickerType datePickerType: Int = DatePickerType.TYPE_YMDHM,
    beginDate: Date? = null,
    endDate: Date? = null,
    onDateChanged: DateRangePickerCallback? = null,
    callback: DateRangePickerCallback? = null,

    func: (DateRangeController.() -> Unit)? = null
) {
    customView(titleRes, title, iconRes, icon) {
        DateRangeController(this@dateRangePicker, this) {
            customView(it)
        }.also {

            it.datePickerType(datePickerType)
            it.dateRange(beginDate, endDate)
            it.onDateChanged(onDateChanged)
            it.callback(callback)


            func?.invoke(it)
        }
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

    private var callback: DateRangePickerCallback? = null
    private var dateChangedCallback: DateRangePickerCallback? = null

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
                dialog,
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

    fun datePickerType(@DatePickerType type: Int) {
        datePicker.datePickerType = type
    }

    fun dateRange(beginDate: Date? = null, endDate: Date? = null) {
        if (beginDate != null) {
            this.beginDate = datePicker.dateFormat.let { it.parse(it.format(beginDate)) }
        }
        if (endDate != null) {
            this.endDate = datePicker.dateFormat.let { it.parse(it.format(endDate)) }
        }
    }

    fun onDateChanged(callback: DateRangePickerCallback?) {
        this.dateChangedCallback = callback
    }

    fun callback(callback: DateRangePickerCallback?) {
        this.callback = callback
    }

    override fun positiveButton(
        textRes: Int?, text: CharSequence?,
        iconRes: Int?, icon: Drawable?,
        colorRes: Int?, color: Int?,
        click: DialogButtonClick?
    ) {
        delegate.positiveButton(textRes, text, iconRes, icon, colorRes, color) {
            callback?.invoke(
                dialog,
                datePicker.dateFormat.let { it.parse(it.format(beginDate)) },
                datePicker.dateFormat.let { it.parse(it.format(endDate)) }
            )
            click?.invoke(dialog)
        }
    }

    private fun selectBegin() {
        selectedBegin = true
        beginText.alpha = 1.0f
        endText.alpha = 0.2f
        datePicker.date = beginDate
    }

    private fun selectEnd() {
        selectedBegin = false
        beginText.alpha = 0.2f
        endText.alpha = 1.0f
        datePicker.date = endDate
    }

}