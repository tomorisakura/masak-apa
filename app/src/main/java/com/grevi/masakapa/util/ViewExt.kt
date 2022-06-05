package com.grevi.masakapa.util

import android.view.View
import com.grevi.masakapa.util.Constant.ONE_FLOAT
import com.grevi.masakapa.util.Constant.ONE_SECOND

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
    this.animate().alpha(ONE_FLOAT).duration = ONE_SECOND
}