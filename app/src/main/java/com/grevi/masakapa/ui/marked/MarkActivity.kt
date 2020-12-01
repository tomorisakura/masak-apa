package com.grevi.masakapa.ui.marked

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.grevi.masakapa.R
import com.grevi.masakapa.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_mark.*

class MarkActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_icon_text)
        supportActionBar?.title = null

        val navHostMark = supportFragmentManager.findFragmentById(R.id.nav_container_mark) as NavHostFragment
        navHostMark.navController
    }
}