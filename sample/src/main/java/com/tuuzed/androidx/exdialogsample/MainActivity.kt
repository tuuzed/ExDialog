package com.tuuzed.androidx.exdialogsample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.style.Circle
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

    private lateinit var cxt: Context
    private lateinit var listAdapter: ListAdapter

    private val material: Boolean get() = cb_material.isChecked

    override fun onCreate(savedInstanceState: Bundle?) {
        if (
            getSharedPreferences("theme", Context.MODE_PRIVATE).getBoolean("dark", false)
        ) {
            setTheme(R.style.AppDarkTheme_NoActionBar)
        } else {
            setTheme(R.style.AppTheme_NoActionBar)
        }
        super.onCreate(savedInstanceState)
        cxt = this
        setContentView(R.layout.activity_main)
        initToolbar()

        listAdapter = ListAdapter()

        listAdapter.bind(ButtonItem::class.java, ButtonItemViewBinder())
        listAdapter.bind(SegmentItem::class.java, SegmentItemViewBinder())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter

        samples()
        listAdapter.notifyDataSetChanged()

    }

    private fun samples() {
        segment("LoadingSamples")
        button("Basic Loading") {
            ExDialog(cxt).show(material) { loading() }
        }
        button("Loading + Message") {
            ExDialog(this).show(material) {
                canceledOnTouchOutside(false)
                loading(text = "加载中...")
                addEventWatcher { _, event ->
                    if (event == ExDialogEvent.ON_DISMISS) {
                        toast("onDismiss")
                    }
                }
            }
        }
        button("Loading + Custom Color") {
            ExDialog(this).show(material) {
                loading(text = "加载中...", iconColor = Color.RED)
            }
        }
        button("Loading + Custom Icon") {
            ExDialog(this).show(material) {
                loading(text = "加载中...", icon = Circle())
            }
        }
        segment("BasicSamples")
        button("Message") {
            ExDialog(this).show(material) {
                title(text = "标题")
                message(text = "这是一条消息。".repeat(20))
                positiveButton()
                negativeButton()
                neutralButton(text = "关闭", color = 0XFF757575.toInt())
            }
        }
        button("Buttons") {
            ExDialog(this).show(material) {
                title(text = "标题")
                message(text = "这是一条消息。".repeat(10))
                positiveButton(text = "positiveButton positiveButton")
                negativeButton(text = "negativeButton negativeButton")
                neutralButton(text = "neutralButton neutralButton")
            }
        }
        button("Long Message") {
            ExDialog(this).show(material) {
                message(text = "这是一条很长的消息，会重复很多次。".repeat(1000))
                positiveButton(text = "关闭")
            }
        }
        segment("Lists")
        button("Items") {
            ExDialog(this).show(material) {
                val items = (1..2).map { "Item$it" }
                items(
                    items = items.toTypedArray(),
                    callback = { _, index ->
                        toast("index: $index")
                    }
                )
            }
        }
        button("Long Items") {
            ExDialog(this).show(material) {
                title(text = "标题")
                val items = (1..100).map { "Item$it" }
                items(
                    items = items.toTypedArray(),
                    callback = { _, index ->
                        toast("index: $index")
                    }
                )
                negativeButton()
            }
        }
        button("Single Choice Items") {
            ExDialog(this).show(material) {
                title(text = "标题")
                val items = (1..4).map { "Item$it" }
                singleChoiceItems(
                    items = items.toTypedArray(),
                    watcher = { _, index ->
                        toast("index: $index")
                    },
                    callback = { _, index ->
                        toast("index: $index")
                    }
                )
                positiveButton()
                negativeButton()
            }
        }
        button("Multi Choice Items") {
            ExDialog(this).show(material) {
                title(text = "标题")
                val items = (1..4).map { "Item$it" }
                multiChoiceItems(
                    items = items.toTypedArray(),
                    checkedIndices = intArrayOf(1, 3),
                    watcher = { _, index, isChecked ->
                        toast("index: $index, isChecked: $isChecked")
                    },
                    callback = { _, indices ->
                        toast("indices: ${Arrays.toString(indices)}")
                    }
                )
                positiveButton()
                negativeButton()
            }
        }
        segment("InputSamples")
        button("Input") {
            ExDialog(this).show(material) {
                title(text = "标题")
                input(
                    hint = "Hint",
                    helperText = "Helper",
                    maxLength = 10,
                    callback = { _, text ->
                        toast("$text")
                    }
                )
                positiveButton()
                negativeButton()
            }
        }
        button("Input Not Allow Empty") {
            ExDialog(this).show(material) {
                title(text = "标题")
                input(
                    allowEmpty = false,
                    callback = { _, text ->
                        toast("$text")
                    }
                )
                positiveButton()
            }
        }
        button("Input Validator") {
            ExDialog(this).show(material) {
                title(text = "标题")
                input(
                    validator = { _, text, errorText ->
                        try {
                            text.toString().toInt()
                            false
                        } catch (e: Exception) {
                            errorText[0] = e.message.toString()
                            true
                        }
                    },
                    callback = { _, text ->
                        toast("$text")
                    }
                )
                positiveButton()
                negativeButton()
            }
        }
        segment("DatePicker")
        button("Date Picker") {
            ExDialog(this).show(material) {
                title(text = "标题")
                datePicker(
                    callback = { _, date ->
                        toast(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date))
                    }
                )
                negativeButton()
                positiveButton()
            }
        }
        button("Date Range Picker") {
            ExDialog(this).show(material) {
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
        }
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

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).also {
            it.setGravity(Gravity.CENTER, 0, 0)
        }.show()
    }

    private fun button(text: String = "", itemClick: () -> Unit) {
        listAdapter.items.add(ButtonItem(text, itemClick))
    }

    private fun segment(text: String = "") {
        listAdapter.items.add(SegmentItem(text))
    }

    private class ButtonItem(var text: String, var itemClick: () -> Unit)

    private class ButtonItemViewBinder : ItemViewBinder.Factory<ButtonItem, CommonViewHolder>() {
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
    }

    private class SegmentItem(var text: String)
    private class SegmentItemViewBinder : ItemViewBinder.Factory<SegmentItem, CommonViewHolder>() {
        override fun getLayoutRes(): Int = R.layout.main_listitem_segment
        override fun createViewHolder(itemView: View) = CommonViewHolder(itemView)
        override fun onBindViewHolder(holder: CommonViewHolder, item: SegmentItem, position: Int) {
            holder.withView<TextView>(R.id.text) {
                text = item.text
            }
        }
    }


}
