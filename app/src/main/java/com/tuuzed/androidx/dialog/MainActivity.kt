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
import com.tuuzed.androidx.dialog.ex.basic.basic
import com.tuuzed.androidx.dialog.ex.basic.message
import com.tuuzed.androidx.dialog.ex.date.date
import com.tuuzed.androidx.dialog.ex.date.dateRange
import com.tuuzed.androidx.dialog.ex.input.input
import com.tuuzed.androidx.dialog.ex.lifecycle.lifecycleOwner
import com.tuuzed.androidx.dialog.ex.lists.simpleItems
import com.tuuzed.androidx.dialog.ex.loading.loading
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

        val loading = ExDialog.show(this) { loading {} }.lifecycleOwner(this)
        toolbar.postDelayed({ loading.dismiss() }, 1500)

        SegmentItem("ExDialog").also { listAdapter.appendItems(it) }
        ButtonItem("Loading") {
            ExDialog.show(this) {
                windowAnimations(ExDialog.WINDOW_ANIMATION_FADE)
                canceledOnTouchOutside(false)
                onDismiss { toast("onDismiss") }
                loading { text(text = "加载中...") }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Basic") {
            ExDialog.show(this) {
                basic {
                    icon(R.mipmap.ic_launcher)
                    title("标题")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Message") {
            ExDialog.show(this) {
                message {
                    title("标题")
                    message("这是一条消息。")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Long Message") {
            ExDialog.show(this) {
                message {
                    title("标题")
                    message("这是一条很长的消息。".let {
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
            ExDialog.show(this) {
                input {
                    title("标题")
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
            ExDialog.show(this) {
                val items = mutableListOf<String>()
                for (i in 1..4) items.add("Item$i")
                simpleItems {
                    items(items)
                    callback { dialog, item, position ->
                        toast("item: $item, position: $position")
                        dialog.dismiss()
                    }
                }
            }
        }.also { listAdapter.appendItems(it) }

        ButtonItem("Lazy Items") {
            ExDialog.show(this) {
                simpleItems {
                    canceledOnTouchOutside(false)
                    title("Lazy Items")
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
                                showClickButton("加载失败，点击重试。", 0xFF5DA017.toInt()) {
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
                    callback { dialog, item, position ->
                        toast("item: $item, position: $position")
                        dialog.dismiss()
                    }
                    onDismiss { loadTask.safeCancel() }
                    positiveButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Long Items") {
            ExDialog.show(this) {
                val items = mutableListOf<String>()
                for (i in 1..100) items.add("Item$i")
                simpleItems {
                    items(items)
                    callback { dialog, item, position ->
                        toast("item: $item, position: $position")
                        dialog.dismiss()
                    }
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Items + Title + Buttons") {
            ExDialog.show(this) {
                val items = mutableListOf<String>()
                for (i in 1..4) items.add("Item$i")
                simpleItems {
                    title("标题")
                    items(items)
                    callback { dialog, selected, index ->
                        toast("selected: $selected, index: $index")
                        dialog.dismiss()
                    }
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Long Items + Title + Buttons") {
            ExDialog.show(this) {
                val items = mutableListOf<String>()
                for (i in 1..4) items.add("Item$i")
                simpleItems {
                    title("标题")
                    items(items)
                    callback { dialog, selected, index ->
                        toast("selected: $selected, index: $index")
                        dialog.dismiss()
                    }
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }

        SegmentItem("Date").also { listAdapter.appendItems(it) }
        ButtonItem("Date") {
            ExDialog.show(this) {
                date {
                    title("标题")
                    callback {
                        toast(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(it))
                    }
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Date Range") {
            ExDialog.show(this) {
                dateRange {
                    callback { beginDate, endDate ->
                        toast(
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(beginDate)
                                    + " ~ " +
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(endDate)
                        )
                    }
                    title("标题")
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
