package com.grevi.masakapa.ui.base

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.grevi.masakapa.R
import com.grevi.masakapa.util.Constant
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import java.util.zip.Inflater

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private lateinit var _binding : VB
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
        subscribeUI()
        storageHandler()
        setupNavigation(onBindView = {
            navigationStateView(it)
        }, onNegativeView = {
            navigationOnDisableView()
        })
    }

    private fun setupNavigation(
        onBindView : (nav :NavController) -> Unit,
        onNegativeView: () -> Unit
    ) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, _ ->
            when(destination.id) {
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
        when(item.itemId) {
            R.id.dayNight -> {
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

    private fun storageHandler() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onExplainRequestReason { scope, deniedList, _ ->
                scope.showRequestReasonDialog(deniedList,
                    getString(R.string.bucket_permission_reason),
                    "Ok",
                    "Cancel")
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    this.getSharedPreferences(Constant.PERMISSIONS_STORAGE, Context.MODE_PRIVATE)
                        .apply {
                        edit().let {
                            it.putBoolean(Constant.PERMISSIONS_STORAGE, true)
                            it.apply()
                        }
                    }
                }
            }
    }
}