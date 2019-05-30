package com.tuuzed.androidx.exdialogsample

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.ExDialogEvent
import com.tuuzed.androidx.exdialog.datepicker.datePicker
import com.tuuzed.androidx.exdialog.datepicker.dateRangePicker
import com.tuuzed.androidx.exdialog.input.input
import com.tuuzed.androidx.exdialog.loading.loading
import com.tuuzed.androidx.list.adapter.CommonViewHolder
import com.tuuzed.androidx.list.adapter.ItemViewBinder
import com.tuuzed.androidx.list.adapter.ListAdapter
import com.tuuzed.androidx.list.adapter.ktx.withView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        if (
            getSharedPreferences("theme", Context.MODE_PRIVATE).getBoolean("dark", false)
        ) {
            setTheme(R.style.AppDarkTheme_NoActionBar)
        } else {
            setTheme(R.style.AppTheme_NoActionBar)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initRecyclerView()
        loadingSamples()
        basicSamples()
        inputSamples()
        listsSamples()
        datePickerSamples()
        listAdapter.notifyDataSetChanged()

    }

    private fun loadingSamples() {
        SegmentItem("LoadingSamples").also { listAdapter.items.add(it) }
        ButtonItem("Loading ") {
            ExDialog(this).show {
                loading()
            }
        }.also { listAdapter.items.add(it) }
        ButtonItem("Loading + Message") {
            ExDialog(this).show {
                canceledOnTouchOutside(false)
                loading(text = "加载中...")
                addEventWatcher { _, event ->
                    if (event == ExDialogEvent.ON_DISMISS) {
                        toast("onDismiss")
                    }
                }
            }
        }.also { listAdapter.items.add(it) }
    }

    private fun basicSamples() {
        SegmentItem("BasicSamples").also { listAdapter.items.add(it) }
        ButtonItem("Message") {
            ExDialog(this).show {
                title(text = "标题")
                message(text = "这是一条很长的消息。".repeat(1000))
                positiveButton()
                negativeButton()
                neutralButton(text = "关闭", color = 0XFF757575.toInt())
            }
        }.also { listAdapter.items.add(it) }
        ButtonItem("Long Message") {
            ExDialog(this).show {
                message(text = "这是一条很长的消息。".repeat(1000))
                positiveButton(text = "关闭")
            }
        }.also { listAdapter.items.add(it) }
    }

    private fun inputSamples() {
        SegmentItem("InputSamples").also { listAdapter.items.add(it) }
        ButtonItem("Input") {
            ExDialog(this).show {
                title(text = "标题")
                input(
                    hint = "Hint",
                    helperText = "Helper",
                    callback = { _, text ->
                        toast("$text")
                    }
                )
                positiveButton()
                negativeButton()
            }
        }.also { listAdapter.items.add(it) }
        ButtonItem("Non Empty Input") {
            ExDialog(this).show {
                title(text = "标题")
                input(
                    allowEmpty = false,
                    callback = { _, text ->
                        toast("$text")
                    }
                )
                positiveButton()
            }
        }.also { listAdapter.items.add(it) }
    }

    private fun listsSamples() {
        SegmentItem("Lists").also { listAdapter.items.add(it) }
        ButtonItem("Items") {
            ExDialog(this).show {
                val items = (1..2).map { "Item$it" }
                items(
                    items = items.toTypedArray(),
                    callback = { _, index ->
                        toast("index: $index")
                    }
                )
            }
        }.also { listAdapter.items.add(it) }
        ButtonItem("Long Items") {
            ExDialog(this).show {
                title(text = "标题")
                val items = (1..100).map { "Item$it" }
                items(
                    items = items.toTypedArray(),
                    callback = { _, index ->
                        toast("index: $index")
                    }
                )
                positiveButton()
                negativeButton()
            }
        }.also { listAdapter.items.add(it) }
        ButtonItem("Items + Title + Buttons") {
            ExDialog(this).show {
                title(text = "标题")
                val items = (1..4).map { "Item$it" }
                items(
                    items = items.toTypedArray(),
                    callback = { _, index ->
                        toast("index: $index")
                    }
                )
                positiveButton()
                negativeButton()
            }
        }.also { listAdapter.items.add(it) }

        ButtonItem("Single Choice Items") {
            ExDialog(this).show {
                title(text = "标题")
                val items = (1..4).map { "Item$it" }
                singleChoiceItems(
                    items = items.toTypedArray(),
                    watcher = { _, index ->
                        toast("index: $index")
                    },
                    callback = { _, indices ->
                        toast("indices: $indices")
                    }
                )
                positiveButton()
                negativeButton()
            }
        }.also { listAdapter.items.add(it) }
        ButtonItem("Multi Choice Items") {
            ExDialog(this).show {
                title(text = "标题")
                val items = (1..4).map { "Item$it" }
                multiChoiceItems(
                    items = items.toTypedArray(),
                    checkedIndices = intArrayOf(1, 3),
                    watcher = { _, index, isChecked ->
                        toast("index: $index, isChecked: $isChecked")
                    },
                    callback = { _, indices ->
                        toast("indices: $indices")
                    }
                )
                positiveButton()
                negativeButton()
            }
        }.also { listAdapter.items.add(it) }
    }

    private fun datePickerSamples() {
        SegmentItem("DatePicker").also { listAdapter.items.add(it) }
        ButtonItem("Date Picker") {
            ExDialog(this).show {
                title(text = "标题")
                datePicker(
                    callback = { _, date ->
                        toast(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date))
                    }
                )
                negativeButton()
                positiveButton()

            }
        }.also { listAdapter.items.add(it) }
        ButtonItem("Date Range Picker") {
            ExDialog(this).show {
                title(text = "标题")
                dateRangePicker(
                    callback = { _, beginDate, endDate ->
                        toast(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).let {
                            it.format(beginDate) + " ~ " + it.format(endDate)
                        })
                    }
                )
                positiveButton()
                negativeButton()
            }
        }.also { listAdapter.items.add(it) }
    }

    private fun initToolbar() {
        toolbar.also {
            it.title = getString(R.string.app_name)
            it.menu.add(0, 1, 0, "切换主题")
            it.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {
                        val dark = getSharedPreferences("theme", Context.MODE_PRIVATE).getBoolean("dark", false)
                        getSharedPreferences("theme", Context.MODE_PRIVATE).edit {
                            putBoolean("dark", !dark)
                        }
                        recreate()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun initRecyclerView() {
        listAdapter = ListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter
        listAdapter.bind(ButtonItem::class.java, object : ItemViewBinder.Factory<ButtonItem, CommonViewHolder>() {
            override fun getLayoutRes(): Int = R.layout.main_listitem_button
            override fun createViewHolder(itemView: View) = CommonViewHolder(itemView)
            override fun onBindViewHolder(holder: CommonViewHolder, item: ButtonItem, position: Int) {
                holder.withView<Button>(R.id.button) {
                    text = item.text
                    setOnClickListener {
                        item.itemClick()
                    }
                }
            }
        })
            .bind(SegmentItem::class.java, object : ItemViewBinder.Factory<SegmentItem, CommonViewHolder>() {
                override fun getLayoutRes(): Int = R.layout.main_listitem_segment
                override fun createViewHolder(itemView: View) = CommonViewHolder(itemView)
                override fun onBindViewHolder(holder: CommonViewHolder, item: SegmentItem, position: Int) {
                    holder.withView<TextView>(R.id.text) {
                        text = item.text
                    }
                }
            })
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).also {
            it.setGravity(Gravity.CENTER, 0, 0)
        }.show()
    }

    private class ButtonItem(var text: String, var itemClick: () -> Unit)

    private class SegmentItem(var text: String)
}
