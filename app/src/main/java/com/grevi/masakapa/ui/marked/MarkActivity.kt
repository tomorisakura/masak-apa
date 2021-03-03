package com.grevi.masakapa.ui.marked

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ActivityMarkBinding
import com.grevi.masakapa.ui.base.BaseActivity

class MarkActivity : BaseActivity() {
    private lateinit var binding : ActivityMarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setIcon(R.drawable.ic_icon_text)
        supportActionBar?.title = null

        val navHostMark = supportFragmentManager.findFragmentById(R.id.nav_container_mark) as NavHostFragment
        navHostMark.navController
    }
}