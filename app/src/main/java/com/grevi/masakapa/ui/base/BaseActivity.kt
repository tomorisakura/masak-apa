package com.grevi.masakapa.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        supportActionBar?.hide()
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        Log.v("BASE_ACTIVITY", "BASE RUN")
    }
}