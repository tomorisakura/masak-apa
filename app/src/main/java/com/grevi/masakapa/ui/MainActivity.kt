package com.grevi.masakapa.ui

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.grevi.masakapa.R
import com.grevi.masakapa.common.base.BaseActivity
import com.grevi.masakapa.common.shared.SharedPref
import com.grevi.masakapa.databinding.ActivityHomeBinding
import com.grevi.masakapa.util.Constant
import com.grevi.masakapa.util.hide
import com.grevi.masakapa.util.show
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityHomeBinding>() {

    @Inject
    lateinit var sharedPref: SharedPref

    override fun subscribeUI() = Unit

    override fun getViewBindingInflater(inflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(inflater)
    }

    override fun observeNavigation(controller: NavController, destination: NavDestination) =
        with(binding) {
            when (destination.id) {
                R.id.splashFragment -> setupAppBar(true, controller)
                R.id.recipesFragment -> {
                    appBar.show()
                    appBar.setupSearchView(true)
                    appBar.setSearchViewClicked { controller.navigate(R.id.searchFragment2) }
                }
                R.id.searchFragment2 -> setupAppBar(false, controller)
                R.id.markFragment2 -> setupAppBar(false, controller)
                R.id.detailFragment -> setupAppBar(false, controller)
            }
        }

    private fun loadDarkMode() {
        if (sharedPref.getSharedDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPref.setSharedDarkMode(false)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPref.setSharedDarkMode(true)
        }
    }

    private fun setupAppBar(state: Boolean, controller: NavController) = with(binding.appBar) {
        onFavoriteClickListener { controller.navigate(R.id.markFragment2) }
        onBackClickListener { controller.popBackStack() }
        onDayNightClickListener {
            sharedPref.setSharedDarkMode(sharedPref.getSharedDarkMode())
            loadDarkMode()
        }
        if (state) hide()
        else {
            show()
            setupSearchView(false)
        }
    }

}