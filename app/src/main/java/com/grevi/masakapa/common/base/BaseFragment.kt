package com.grevi.masakapa.common.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.grevi.masakapa.R
import com.grevi.masakapa.common.dialog.DialogBottomSheet
import com.grevi.masakapa.util.Constant.TWO_SECOND
import com.grevi.masakapa.common.network.Network
import com.grevi.masakapa.common.factory.ViewModelFactory
import com.grevi.masakapa.common.popup.snackBar
import com.grevi.masakapa.util.Constant.DIALOG_TAG
import javax.inject.Inject

abstract class BaseFragment<VB: ViewBinding, VM: ViewModel> : Fragment() {
    private lateinit var _binding: VB
    protected val binding get() = _binding

    @Inject
    lateinit var factory : ViewModelFactory

    private lateinit var _viewModels: VM
    protected val viewModels get() = _viewModels

    private lateinit var _context: Context
    protected val baseContext get() = _context

    private val networkUtils by lazy { Network(this.context ?: _context) }

    private lateinit var _navController: NavController
    val navController get() = _navController

    protected val snapHelper: LinearSnapHelper by lazy { LinearSnapHelper() }

    private val noInternetDialogAlert by lazy {
        DialogBottomSheet.newInstance(
            title = getString(R.string.no_inet_title),
            description = getString(R.string.no_inet_text)
        )
    }

    abstract fun getViewModelClass(): Class<VM>
    abstract fun getViewBindingInflater(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun subscribeUI()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _viewModels = ViewModelProvider(this, factory)[getViewModelClass()]
        _context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBindingInflater(inflater, container)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        _navController = Navigation.findNavController(view)
        observeNetworkState()
    }

    protected fun onSwipeRefresh(
        view: SwipeRefreshLayout?,
        pg : LinearProgressIndicator?,
    ) {
        networkUtils.networkDataStatus.observe(viewLifecycleOwner) {
            view?.setOnRefreshListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    subscribeUI()
                    pg?.visibility = View.GONE
                }, TWO_SECOND)
            }
        }
    }

    private fun observeNetworkState() {
        networkUtils.networkDataStatus.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) subscribeUI()
            else noInternetDialogAlert.show(childFragmentManager, DIALOG_TAG)
        }
    }

    @JvmName("getBaseBinding")
    fun getBinding() = binding
}