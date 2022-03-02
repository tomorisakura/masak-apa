package com.grevi.masakapa.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.grevi.masakapa.R
import com.grevi.masakapa.ui.HomeActivity
import com.grevi.masakapa.util.Constant.TWO_SECOND
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            Intent(this, HomeActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }, TWO_SECOND)
    }
}