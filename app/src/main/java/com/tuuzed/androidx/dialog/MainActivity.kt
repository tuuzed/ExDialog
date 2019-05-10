package com.tuuzed.androidx.dialog

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tuuzed.androidx.dialog.ext.*
import kotlinx.android.synthetic.main.main_act.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)
        setSupportActionBar(toolbar)

        val loading = ExDialog.show(this) { loading {} }.lifecycleOwner(this)

        toolbar.postDelayed({ loading.dismiss() }, 1500)

        // ExDialog
        btn_loading_dialog.setOnClickListener {
            ExDialog.show(this) {
                windowAnimations(ExDialog.WINDOW_ANIMATION_FADE)
                canceledOnTouchOutside(false)
                onDismissEvent {
                    Toast.makeText(this@MainActivity, "DismissEvent", Toast.LENGTH_SHORT).show()
                }
                loading { text(text = "加载中...") }
            }
        }
        btn_basic_dialog.setOnClickListener {
            ExDialog.show(this) {
                basic {
                    title("标题")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }
        btn_message_dialog.setOnClickListener {
            ExDialog.show(this) {
                message {
                    title("标题")
                    message("这是一条消息。")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }
        btn_long_message_dialog.setOnClickListener {
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
        }
        btn_input_dialog.setOnClickListener {
            ExDialog.show(this) {
                input {
                    title("标题")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }
        btn_items_dialog.setOnClickListener {
            ExDialog.show(this) {
                items {
                    title("标题")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }

        btn_date_range_dialog.setOnClickListener {
            ExDialog.show(this) {
                dateRangeChooser {
                    title("标题")
                    positiveButton("确定", 0xFFFF5722.toInt())
                    negativeButton("取消", 0XFF80CBC4.toInt())
                    neutralButton("关闭", 0XFF757575.toInt())
                }
            }
        }
        // System
        btn_system_dialog.setOnClickListener {
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
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
