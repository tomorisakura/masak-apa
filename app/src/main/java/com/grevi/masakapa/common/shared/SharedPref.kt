package com.grevi.masakapa.common.shared

import android.content.Context
import android.content.SharedPreferences
import com.grevi.masakapa.common.base.BaseFragment
import javax.inject.Inject

class SharedPref @Inject constructor(private val context: Context) {
    fun getStoragePermission(key: String) =
        context.getSharedPreferences(key, Context.MODE_PRIVATE).getBoolean(key, false)

    fun getSharedDarkMode(key: String) =
        context.getSharedPreferences(key, Context.MODE_PRIVATE).getBoolean(key, false)
}