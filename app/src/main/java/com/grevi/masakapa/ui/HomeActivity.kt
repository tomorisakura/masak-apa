package com.grevi.masakapa.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ActivityHomeBinding
import com.grevi.masakapa.ui.base.BaseActivity
import com.grevi.masakapa.util.Constant.PERMISSIONS_STORAGE
import com.permissionx.guolindev.PermissionX

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
        storageHandler()
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
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onExplainRequestReason { scope, deniedList, _ ->
                scope.showRequestReasonDialog(deniedList, getString(R.string.bucket_permission_reason), "Ok", "Cancel")
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    this.getSharedPreferences(PERMISSIONS_STORAGE, Context.MODE_PRIVATE).apply {
                        edit().let {
                            it.putBoolean(PERMISSIONS_STORAGE, true)
                            it.apply()
                        }
                    }
                }
            }
    }

}