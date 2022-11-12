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
import com.grevi.masakapa.common.network.Network
import com.grevi.masakapa.common.popup.snackBar
import com.grevi.masakapa.util.Constant.TWO_SECOND
import kotlinx.coroutines.Job
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {
    private lateinit var _binding: VB
    protected val binding get() = _binding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var _viewModel: VM
    protected val viewModel get() = _viewModel

    private lateinit var _context: Context
    protected val baseContext get() = _context

    private val networkUtils by lazy { Network(this.context ?: _context) }

    private lateinit var _navController: NavController
    val navController get() = _navController

    protected val snapHelper: LinearSnapHelper by lazy { LinearSnapHelper() }

    internal val job by lazy { Job() }

    abstract fun getViewModelClass(): Class<VM>
    abstract fun getViewBindingInflater(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun subscribeUI()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _viewModel = ViewModelProvider(this, factory)[getViewModelClass()]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBindingInflater(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        _navController = Navigation.findNavController(view)
        observeNetworkState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    override fun onResume() {
        super.onResume()
        job.start()
    }

    protected fun onSwipeRefresh(
        view: SwipeRefreshLayout?,
        pg: LinearProgressIndicator?,
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
            else snackBar(binding.root, getString(R.string.no_inet_title)).show()
        }
    }

    @JvmName("getBaseBinding")
    fun getBinding() = binding
}