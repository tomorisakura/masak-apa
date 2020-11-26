package com.grevi.masakapa.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.grevi.masakapa.R
import com.grevi.masakapa.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_icon_text)
        supportActionBar?.title = null

        //init navhost container
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navHostFragment.navController
    }
}