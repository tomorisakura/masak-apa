package com.grevi.masakapa.ui

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavController
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ActivityHomeBinding
import com.grevi.masakapa.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun subscribeUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setIcon(R.drawable.ic_icon_text)
        supportActionBar?.title = null
    }

    override fun getViewBindingInflater(inflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(inflater)
    }

    override fun navigationStateView(nav: NavController) = with(binding) {
        searchCard.visibility = View.VISIBLE
        searchCard.setOnClickListener {
            nav.navigate(R.id.searchFragment2)
        }
    }

    override fun navigationOnDisableView() = with(binding) {
        searchCard.visibility = View.GONE
    }

}