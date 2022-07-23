package com.grevi.masakapa.ui

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ActivityHomeBinding
import com.grevi.masakapa.common.base.BaseActivity
import com.grevi.masakapa.util.hide
import com.grevi.masakapa.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun subscribeUI() = Unit

    override fun getViewBindingInflater(inflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(inflater)
    }

    override fun observeNavigation(controller: NavController, destination: NavDestination) = with(binding) {
        when(destination.id) {
            R.id.splashFragment -> setupAppBar(true)
            R.id.recipesFragment -> {
                appBar.show()
                appBar.setupSearchView(true)
                appBar.setSearchViewClicked { controller.navigate(R.id.searchFragment2) }
            }
            R.id.searchFragment2 -> setupAppBar(false)
            R.id.markFragment2 -> setupAppBar(false)
            R.id.detailFragment -> setupAppBar(false)
        }
    }

    private fun setupAppBar(state: Boolean) = with(binding.appBar) {
        if (state) {
            hide()
        } else {
            show()
            setupSearchView(false)
        }
    }

}