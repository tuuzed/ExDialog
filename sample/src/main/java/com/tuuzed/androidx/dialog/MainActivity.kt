package com.tuuzed.androidx.dialog

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
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
        if (
            getSharedPreferences("theme", Context.MODE_PRIVATE).getBoolean("dark", false)
        ) {
            setTheme(R.style.AppDarkTheme_NoActionBar)
        } else {
            setTheme(R.style.AppTheme_NoActionBar)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)
        initToolbar()
        initRecyclerView()
        loadingSamples()
        basicSamples()
        inputSamples()
        listsSamples()
        datePickerSamples()
        listAdapter.notifyDataSetChanged()
    }

    private fun initToolbar() {
        toolbar.also {
            it.title = getString(R.string.app_name)
            it.menu.add(0, 1, 0, "日间模式")
            it.menu.add(0, 2, 0, "夜间模式")
            it.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {
                        getSharedPreferences("theme", Context.MODE_PRIVATE).edit {
                            putBoolean("dark", false)
                        }
                        restartActivity()
                        true
                    }
                    2 -> {
                        getSharedPreferences("theme", Context.MODE_PRIVATE).edit {
                            putBoolean("dark", true)
                        }
                        restartActivity()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun loadingSamples() {
        SegmentItem("LoadingSamples").also { listAdapter.appendItems(it) }
        ButtonItem("Loading ") {
            ExDialog(this).show {
                windowAnimations(ExDialog.WINDOW_ANIMATION_FADE)
                loading()
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Loading + Message") {
            ExDialog(this).show {
                windowAnimations(ExDialog.WINDOW_ANIMATION_FADE)
                canceledOnTouchOutside(false)
                onDialogDismiss { toast("onDismiss") }
                loading(text = "加载中...")
            }
        }.also { listAdapter.appendItems(it) }
    }

    private fun basicSamples() {
        SegmentItem("BasicSamples").also { listAdapter.appendItems(it) }
        ButtonItem("Basic") {
            ExDialog(this).show {
                basic(title = "标题", iconRes = R.mipmap.ic_launcher) {
                    positiveButton()
                    negativeButton()
                    neutralButton(text = "关闭")
                    neutralButtonEnable(false)
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Message") {
            ExDialog(this).show {
                message(title = "标题", message = "这是一条消息。") {
                    positiveButton()
                    negativeButton()
                    neutralButton(text = "关闭", color = 0XFF757575.toInt())
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Long Message") {
            ExDialog(this).show {
                val longMessage = "这是一条很长的消息。".let {
                    var s = it
                    for (i in 0..1000) s += it
                    s
                }
                message(title = "标题", message = longMessage) {
                    positiveButton(text = "关闭")
                }
            }

        }.also { listAdapter.appendItems(it) }
    }

    private fun inputSamples() {
        SegmentItem("InputSamples").also { listAdapter.appendItems(it) }
        ButtonItem("Input") {
            ExDialog(this).show {
                input(
                    title = "标题",
                    hint = "Hint",
                    helperText = "Helper",
                    callback = { _, text ->
                        toast("$text")
                    }
                ) {
                    positiveButton()
                    negativeButton()
                }

            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Non Empty Input") {
            ExDialog(this).show {
                input(
                    title = "标题",
                    callback = { _, text ->
                        toast("$text")
                    }
                ) {
                    title(text = "标题")
                    onTextChanged { _, text ->
                        positiveButtonEnable(text.isNotEmpty())
                    }
                    positiveButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
    }

    private fun listsSamples() {
        SegmentItem("Lists").also { listAdapter.appendItems(it) }
        ButtonItem("Items") {
            ExDialog(this).show {
                val items = (1..2).map { "Item$it" }
                simpleItems(
                    items = items,
                    onClickItem = { dialog, index, checkedItem ->
                        toast("index: $index, checkedItem: $checkedItem")
                        dialog.dismiss()
                    }
                )
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Lazy Items") {
            ExDialog(this).show {
                cancelable(false)
                simpleItems<String>(
                    title = "Lazy Items",
                    onClickItem = { dialog, index, checkedItem ->
                        toast("index: $index, checkedItem: $checkedItem")
                        dialog.dismiss()
                    }
                ) {
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
                    onDialogDismiss { loadTask.safeCancel() }
                    negativeButton { it.dismiss() }
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Long Items") {
            ExDialog(this).show {
                val items = (1..100).map { "Item$it" }
                simpleItems(
                    title = "标题",
                    items = items,
                    onClickItem = { dialog, index, checkedItem ->
                        toast("index: $index, checkedItem: $checkedItem")
                        dialog.dismiss()
                    }
                ) {
                    positiveButton()
                    negativeButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Items + Title + Buttons") {
            ExDialog(this).show {
                val items = (1..4).map { "Item$it" }
                simpleItems(
                    title = "标题",
                    items = items,
                    onClickItem = { dialog, index, checkedItem ->
                        toast("index: $index, checkedItem: $checkedItem")
                        dialog.dismiss()
                    }
                ) {
                    positiveButton()
                    negativeButton()
                }
            }
        }.also { listAdapter.appendItems(it) }

        ButtonItem("Single Choice Items") {
            ExDialog(this).show {
                val items = (1..4).map { "Item$it" }
                singleChoiceItems(
                    title = "标题",
                    items = items,
                    disableIndices = listOf(0),
                    onSelectedItemChanged = { _, selectedIndex, selectedItem ->
                        toast("selectedIndex: $selectedIndex, selectedItem: $selectedItem")
                    },
                    callback = { _, selectedIndex, selectedItem ->
                        toast("selectedIndex: $selectedIndex, selected: $selectedItem")
                    }
                ) {
                    positiveButton()
                    negativeButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Multi Choice Items") {
            ExDialog(this).show {
                val items = (1..4).map { "Item$it" }
                multiChoiceItems(
                    title = "标题",
                    items = items,
                    selectedIndices = listOf(1, 3),
                    onSelectedItemChanged = { _, selectedIndices, selectedItems ->
                        toast("selectedIndices: $selectedIndices, selectedItems: $selectedItems")
                    },
                    callback = { _, selectedIndices, selectedItems ->
                        toast("selectedIndices: $selectedIndices, selectedItems: $selectedItems")
                    }
                ) {
                    positiveButton()
                    negativeButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
    }

    private fun datePickerSamples() {
        SegmentItem("DatePicker").also { listAdapter.appendItems(it) }
        ButtonItem("Date Picker") {
            ExDialog(this).show {
                datePicker(
                    title = "标题",
                    callback = { _, date ->
                        toast(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date))
                    }
                ) {
                    positiveButton()
                    negativeButton()
                }
            }
        }.also { listAdapter.appendItems(it) }
        ButtonItem("Date Range Picker") {
            ExDialog(this).show {
                dateRangePicker(
                    title = "标题",
                    callback = { _, beginDate, endDate ->
                        toast(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).let {
                            it.format(beginDate) + " ~ " + it.format(endDate)
                        })
                    }
                ) {
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


    private fun restartActivity() {
        recreate()
    }

    private class ButtonItem(var text: String, var itemClick: () -> Unit)

    private class SegmentItem(var text: String)


}
