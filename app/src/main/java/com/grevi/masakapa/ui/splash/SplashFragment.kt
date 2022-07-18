package com.grevi.masakapa.ui.splash

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.databinding.FragmentSplashBinding
import com.grevi.masakapa.ui.viewmodel.SplashViewModel
import com.grevi.masakapa.util.Constant.TWO_SECOND
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun getViewBindingInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun subscribeUI() {
        handler.postDelayed({
            navigateToHome()
        }, TWO_SECOND)
    }

    private fun navigateToHome() {
        navController.navigate(
            SplashFragmentDirections.actionSplashFragmentToRecipesFragment()
        )
    }
}