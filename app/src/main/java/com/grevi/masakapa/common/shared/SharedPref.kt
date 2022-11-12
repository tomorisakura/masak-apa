package com.grevi.masakapa.common.shared

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.util.Constant
import javax.inject.Inject

class SharedPref @Inject constructor(private val context: Context) {

    private val prefManager = PreferenceManager.getDefaultSharedPreferences(context)

    fun setStoragePermission(value: Boolean) =
        prefManager.edit {
            putBoolean(Constant.PERMISSIONS_STORAGE, value)
            commit()
        }

    fun setSharedDarkMode(value: Boolean) =
        prefManager.edit {
            putBoolean(Constant.DAY_LIGHT_STATE, value)
            commit()
        }

    fun getStoragePermission(key: String) =
        prefManager.getBoolean(Constant.PERMISSIONS_STORAGE, false)

    fun getSharedDarkMode() = prefManager.getBoolean(Constant.DAY_LIGHT_STATE, false)
}