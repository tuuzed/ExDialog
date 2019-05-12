package com.tuuzed.androidx.dialog

import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        val loading = ExDialog(this).show { loading {} }.lifecycleOwner(this)
        toolbar.postDelayed({ loading.dismiss() }, 1500)

        SegmentItem("ExDialog").also { listAdapter.appendItems(it) }
        ButtonItem("Loading") {
            ExDialog(this).show {
                loading {
                    windowAnimations(ExDialog.WINDOW_ANIMATION_FADE)
                    canceledOnTouchOutside(false)
                    onDialogDismiss { toast("onDismiss") }
                    text("加载中...")
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Basic") {
            ExDialog(this).show {
                basic {
                    icon(R.mipmap.ic_launcher)
                    title(text = "标题")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                    disableNeutralButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Message") {
            ExDialog(this).show {
                message {
                    title(text = "标题")
                    message(text = "这是一条消息。")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
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
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }

        }.also { listAdapter.appendItems(it) }
        ButtonItem("Input") {

            ExDialog(this).show {
                input {
                    title(text = "标题")
                    callback {
                        toast("$it")
                    }
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }

        SegmentItem("Lists").also { listAdapter.appendItems(it) }

        ButtonItem("Items") {
            ExDialog(this).show {
                simpleItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..4) items.add("Item$i")
                    items(items)
                    itemClick { dialog, index, item ->
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
                                positiveButton("确定") { dialog, _ ->
                                    dialog.dismiss()
                                }
                            }
                        }
                    }
                    val loadTask = lazyLoadTask()
                    itemClick { dialog, index, item ->
                        toast("index: $index, item: $item")
                        dialog.dismiss()
                    }
                    onDialogDismiss { loadTask.safeCancel() }
                    positiveButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Long Items") {
            ExDialog(this).show {
                simpleItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..100) items.add("Item$i")
                    items(items)
                    itemClick { dialog, index, item ->
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
                    itemClick { dialog, index, item ->
                        toast("index: $index, item: $item")
                        dialog.dismiss()
                    }
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
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
                    itemClick { dialog, index, item ->
                        toast("index: $index, item: $item")
                        dialog.dismiss()
                    }
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }

        ButtonItem("Single Choice Items") {
            ExDialog(this).show {
                singleChoiceItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..4) items.add("Item$i")
                    title(text = "标题")
                    items(items)
                    callback { _, index, selectedItem ->
                        toast("index: $index, selected: $selectedItem")
                    }
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }


        ButtonItem("Multi Choice Items") {
            ExDialog(this).show {
                multiChoiceItems<String> {
                    val items = mutableListOf<String>()
                    for (i in 1..4) items.add("Item$i")
                    title(text = "标题")
                    items(items)
                    callback { _, indices, selectedItems ->
                        toast("indices: $indices, selectedItems: $selectedItems")
                    }
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }



        SegmentItem("DatePicker").also { listAdapter.appendItems(it) }
        ButtonItem("Date Picker") {
            ExDialog(this).show {
                datePicker {
                    title(text = "标题")
                    callback {
                        toast(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(it))
                    }
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Date Range Picker") {
            ExDialog(this).show {
                dateRangePicker {
                    callback { beginDate, endDate ->
                        toast(
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(beginDate)
                                    + " ~ " +
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(endDate)
                        )
                    }
                    title(text = "标题")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }



        SegmentItem("System").also { listAdapter.appendItems(it) }
        ButtonItem("System") {
            AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Title")
                .setMultiChoiceItems(
                    arrayOf("Item1", "Item2", "Item3", "Item4"),
                    booleanArrayOf(false, false, false, false)
                ) { _, _, _ -> }
                .setPositiveButton("确定") { _, _ -> }
                .setNegativeButton("取消", null)
                .setNeutralButton("关闭", null)
                .show()
        }.also { listAdapter.appendItems(it) }


        listAdapter.notifyDataSetChanged()

    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).also {
            it.setGravity(Gravity.CENTER, 0, 0)
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private class ButtonItem(var text: String, var itemClick: () -> Unit)

    private class SegmentItem(var text: String)


}
