package com.grevi.masakapa.common.shared

import android.content.Context
import android.content.SharedPreferences
import com.grevi.masakapa.common.base.BaseFragment

fun BaseFragment<*,*>.getStoragePermission(context: Context, key: String): Boolean {
    return context.getSharedPreferences(key, Context.MODE_PRIVATE).getBoolean(key, false)
}