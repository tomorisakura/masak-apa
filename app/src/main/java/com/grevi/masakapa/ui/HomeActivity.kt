package com.grevi.masakapa.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ActivityHomeBinding
import com.grevi.masakapa.ui.base.BaseActivity

class HomeActivity : BaseActivity() {
    private lateinit var binding : ActivityHomeBinding
    private var state = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setIcon(R.drawable.ic_icon_text)
        supportActionBar?.title = null

        //init navhost container
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.dayNight -> {
                //Toast.makeText(this, "state : ${state}", Toast.LENGTH_SHORT).show()
                state = when(state) {
                    true -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        false
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        true
                    }
                }
            }
        }
        //return true
        return super.onOptionsItemSelected(item)
    }

}