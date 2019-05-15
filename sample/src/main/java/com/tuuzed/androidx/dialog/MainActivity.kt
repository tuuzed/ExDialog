package com.tuuzed.androidx.dialog

import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuuzed.androidx.dialog.coroutines.async
import com.tuuzed.androidx.dialog.coroutines.safeCancel
import com.tuuzed.androidx.dialog.coroutines.then
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.ext.*
import com.tuuzed.recyclerview.adapter.AbstractItemViewBinder
import com.tuuzed.recyclerview.adapter.CommonItemViewHolder
import com.tuuzed.recyclerview.adapter.RecyclerViewAdapter
import kotlinx.android.synthetic.main.main_act.*
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    private lateinit var listAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)
        setSupportActionBar(toolbar)
        initRecyclerView()
        loadingSamples()
        basicSamples()
        inputSamples()
        listsSamples()
        datePickerSamples()
        listAdapter.notifyDataSetChanged()

    }


    private fun loadingSamples() {
        SegmentItem("LoadingSamples").also { listAdapter.appendItems(it) }
        ButtonItem("Loading ") {
            ExDialog(this).show {
                loading { windowAnimations(ExDialog.WINDOW_ANIMATION_FADE) }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Loading + Message") {
            ExDialog(this).show {
                loading {
                    windowAnimations(ExDialog.WINDOW_ANIMATION_FADE)
                    canceledOnTouchOutside(false)
                    onDialogDismiss { toast("onDismiss") }
                    text("加载中...")
                }
            }
        }.also { listAdapter.appendItems(it) }
    }

    private fun basicSamples() {
        SegmentItem("BasicSamples").also { listAdapter.appendItems(it) }
        ButtonItem("Basic") {
            ExDialog(this).show {
                basic {
                    icon(R.mipmap.ic_launcher)
                    title(text = "标题")
                    positiveButton()
                    negativeButton()
                    neutralButton(text = "关闭")
                    neutralButtonEnable(false)
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Message") {
            ExDialog(this).show {
                message {
                    title(text = "标题")
                    message(text = "这是一条消息。")
                    positiveButton()
                    negativeButton()
                    neutralButton(text = "关闭")
                    neutralButtonColor(color = 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Long Message") {
            ExDialog(this).show {
                message {
                    title(text = "标题")
                    message(text = "这是一条很长的消息。".let {
                        var s = it
                        for (i in 0..1000) s += it
                        s
                    })
                    positiveButton(text = "关闭")
                }
            }

        }.also { listAdapter.appendItems(it) }

    }

    private fun inputSamples() {
        SegmentItem("InputSamples").also { listAdapter.appendItems(it) }
        ButtonItem("Input") {
            ExDialog(this).show {
                input {
                    title(text = "标题")
                    hint("Hint")
                    helperText("Helper")
                    callback { _, text ->
                        toast("$text")
                    }
                    positiveButton()
                    negativeButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Non Empty Input") {
            ExDialog(this).show {
                input {
                    title(text = "标题")
                    positiveButton()
                    callback { _, text ->
                        toast("$text")
                    }
                    onTextChanged { _, text ->
                        positiveButtonEnable(text.isNotEmpty())
                    }
                }
            }
        }.also { listAdapter.appendItems(it) }
    }

    private fun listsSamples() {
        SegmentItem("Lists").also { listAdapter.appendItems(it) }
        ButtonItem("Items") {
            ExDialog(this).show {
                simpleItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..4) items.add("Item$i")
                    items(items)
                    itemClick { dialog, index, item, checked ->
                        toast("index: $index, item: $item")
                        dialog.dismiss()
                    }
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Lazy Items") {
            ExDialog(this).show {
                simpleItems<String> {
                    canceledOnTouchOutside(false)
                    title(text = "Lazy Items")
                    var lazyLoadTask: (() -> Job)? = null
                    lazyLoadTask = {
                        async {
                            val items = mutableListOf<String>()
                            for (i in 1..Random().nextInt(100)) items.add("Item$i")
                            // 模拟耗时操作
                            SystemClock.sleep(1000)
                            items
                        } then {
                            if (it.size < 50) {
                                showMessageView("加载失败，点击重试。", 0xFF5DA017.toInt()) {
                                    showLoadingView()
                                    lazyLoadTask?.invoke()
                                }
                            } else {
                                items(it)
                                negativeButtonVisible(false)
                                positiveButton(text = "确定") { dialog ->
                                    dialog.dismiss()
                                }
                            }
                        }
                    }
                    val loadTask = lazyLoadTask()
                    itemClick { dialog, index, item, checked ->
                        toast("index: $index, item: $item")
                        dialog.dismiss()
                    }
                    onDialogDismiss { loadTask.safeCancel() }
                    negativeButton { it.dismiss() }
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Long Items") {
            ExDialog(this).show {
                simpleItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..100) items.add("Item$i")
                    items(items)
                    itemClick { dialog, index, item, checked ->
                        toast("index: $index, item: $item")
                        dialog.dismiss()
                    }
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Items + Title + Buttons") {
            ExDialog(this).show {
                simpleItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..4) items.add("Item$i")
                    title(text = "标题")
                    items(items)
                    itemClick { dialog, index, item, checked ->
                        toast("index: $index, item: $item")
                        dialog.dismiss()
                    }
                    positiveButton()
                    negativeButton()
                    neutralButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Long Items + Title + Buttons") {
            ExDialog(this).show {
                simpleItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..4) items.add("Item$i")
                    title(text = "标题")
                    items(items)
                    itemClick { dialog, index, item, checked ->
                        toast("index: $index, item: $item")
                        dialog.dismiss()
                    }
                    positiveButton()
                    negativeButton()
                    neutralButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Single Choice Items") {
            ExDialog(this).show {
                singleChoiceItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..4) items.add("Item$i")
                    title(text = "标题")
                    items(items, disableIndices = listOf(0))
                    onSelectedItemChanged { _, index, selectedItem ->
                        toast("index: $index, selectedItem: $selectedItem")
                    }
                    callback { _, index, selectedItem ->
                        toast("index: $index, selected: $selectedItem")
                    }
                    positiveButton()
                    negativeButton()
                    neutralButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Multi Choice Items") {
            ExDialog(this).show {
                multiChoiceItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..4) items.add("Item$i")
                    title(text = "标题")
                    items(items, listOf(1, 3))
                    onSelectedItemChanged { _, indices, selectedItems ->
                        toast("indices: $indices, selectedItems: $selectedItems")
                    }
                    callback { _, indices, selectedItems ->
                        toast("indices: $indices, selectedItems: $selectedItems")
                    }
                    positiveButton()
                    negativeButton()
                    neutralButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
    }

    private fun datePickerSamples() {
        SegmentItem("DatePicker").also { listAdapter.appendItems(it) }
        ButtonItem("Date Picker") {
            ExDialog(this).show {
                datePicker {
                    title(text = "标题")
                    callback { _, date ->
                        toast(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date))
                    }
                    positiveButton()
                    negativeButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Date Range Picker") {
            ExDialog(this).show {
                dateRangePicker {
                    callback { _, beginDate, endDate ->
                        toast(
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(beginDate)
                                    + " ~ " +
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(endDate)
                        )
                    }
                    title(text = "标题")
                    positiveButton()
                    negativeButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        listAdapter = RecyclerViewAdapter.with(recyclerView)
            .bind(ButtonItem::class.java, object : AbstractItemViewBinder<ButtonItem>() {
                override fun getLayoutId(): Int = R.layout.main_listitem_button
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: ButtonItem, position: Int) {
                    holder.text(R.id.button, item.text)
                    holder.click(R.id.button) { item.itemClick() }
                }
            })
            .bind(SegmentItem::class.java, object : AbstractItemViewBinder<SegmentItem>() {
                override fun getLayoutId(): Int = R.layout.main_listitem_segment
                override fun onBindViewHolder(holder: CommonItemViewHolder, item: SegmentItem, position: Int) {
                    holder.text(R.id.text, item.text)
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
