package com.grevi.masakapa.common.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.grevi.masakapa.R
import com.grevi.masakapa.common.permission.storagePermission

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private lateinit var _binding: VB
    protected val binding get() = _binding

    private var state = false //should save state on shared pref

    abstract fun getViewBindingInflater(inflater: LayoutInflater): VB

    abstract fun navigationStateView(nav: NavController)

    abstract fun navigationOnDisableView()

    abstract fun subscribeUI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBindingInflater(layoutInflater)
        setContentView(_binding.root)
        supportActionBar?.hide()
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        storagePermission()
        subscribeUI()
        setupNavigation(onBindView = {
            navigationStateView(it)
        }, onNegativeView = {
            navigationOnDisableView()
        })
    }

    private fun setupNavigation(
        onBindView: (nav: NavController) -> Unit,
        onNegativeView: () -> Unit
    ) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                R.id.recipesFragment -> onBindView(controller)
                else -> onNegativeView()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dayNight -> {
                state = when (state) {
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