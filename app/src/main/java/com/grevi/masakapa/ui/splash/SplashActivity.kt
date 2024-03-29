package com.grevi.masakapa.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.grevi.masakapa.R
import com.grevi.masakapa.ui.HomeActivity
import com.grevi.masakapa.ui.base.BaseActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            Intent(this, HomeActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }, 2000L)
    }
}