package com.tuuzed.androidx.dialog.ext.interfaces

import android.view.View
import androidx.annotation.LayoutRes

interface CustomViewControllerInterface {

    fun customView(@LayoutRes layoutId: Int)

    fun customView(view: View)

}