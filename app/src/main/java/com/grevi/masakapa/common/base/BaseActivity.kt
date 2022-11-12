package com.grevi.masakapa.common.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.grevi.masakapa.R
import com.grevi.masakapa.common.dialog.DialogBottomSheet
import com.grevi.masakapa.common.network.Network

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private lateinit var _binding: VB
    protected val binding get() = _binding

    private val networkUtils by lazy { Network(this) }

    private val noInternetDialogAlert by lazy {
        DialogBottomSheet.newInstance(
            title = getString(R.string.no_inet_title),
            description = getString(R.string.no_inet_text)
        )
    }

    abstract fun getViewBindingInflater(inflater: LayoutInflater): VB

    abstract fun subscribeUI()

    abstract fun observeNavigation(controller: NavController, destination: NavDestination)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBindingInflater(layoutInflater)
        setContentView(binding.root)
        observeNetwork()
        setupNavigation()
    }

    private fun observeNetwork() {
        networkUtils.networkDataStatus.observe(this) { isActive ->
            if (isActive) subscribeUI()
            else noInternetDialogAlert.show(supportFragmentManager, this::class.java.simpleName)
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, _ ->
            observeNavigation(controller, destination)
        }
    }
}