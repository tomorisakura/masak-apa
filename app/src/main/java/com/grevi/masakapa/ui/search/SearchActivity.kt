package com.grevi.masakapa.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ActivitySearchBinding
import com.grevi.masakapa.ui.base.BaseActivity
import com.grevi.masakapa.ui.marked.MarkActivity


class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setIcon(R.drawable.ic_icon_text)
        supportActionBar?.title = null
        val navHostSearch = supportFragmentManager.findFragmentById(R.id.nav_container_search) as NavHostFragment
        navHostSearch.navController
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mark_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.markToolBar -> {
                val intent = Intent(this, MarkActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}