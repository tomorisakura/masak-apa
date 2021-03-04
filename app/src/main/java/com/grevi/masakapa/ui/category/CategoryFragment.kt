package com.grevi.masakapa.ui.category

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.FragmentCategoryBinding
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.ui.adapter.CategoryItemAdapter
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.NetworkUtils
import com.grevi.masakapa.util.State
import com.grevi.masakapa.util.snackBar
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var binding : FragmentCategoryBinding
    private val arg : CategoryFragmentArgs by navArgs()
    private val recipesViewModel by viewModels<RecipesViewModel>()
    private val categoryItemAdapter: CategoryItemAdapter by lazy { CategoryItemAdapter() }
    private lateinit var navController: NavController
    private val networkUtils : NetworkUtils by lazy { NetworkUtils(requireContext()) }

    private val TAG = CategoryFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, arg.catKey.toString())
        navController = Navigation.findNavController(view)
        observeNetwork()
        swipeRefresh()
    }

    private fun prepareView() = with(binding) {
        rvRecipesCategoryList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvRecipesCategoryList.adapter = categoryItemAdapter
        refreshCatLayout.isRefreshing = true

        rvRecipesCategoryList.animate().alpha(0f).duration = 1000L

        recipesViewModel.categoryResult(arg.catKey!!).observe(
            viewLifecycleOwner, { results ->
                categoryHintText.text = "${arg.catName} (0)"
                when (results) {
                    is State.Loading -> Log.i(TAG, results.msg)
                    is State.Error -> {
                        toast(requireContext(), results.msg)
                        refreshCatLayout.isRefreshing = true
                    }
                    is State.Success -> {
                        results.data.results.let { categoryItemAdapter.addItem(it) }
                        rvRecipesCategoryList.animate().alpha(1f).duration = 1000L
                        refreshCatLayout.isRefreshing = false
                        categoryHintText.text = "${arg.catName} (${results.data.results?.size})"
                    }
                    else -> Log.i(TAG, "")
                }
            })
        categoryItemAdapter.itemTouch = { prepareNavigate(it) }
    }

    private fun swipeRefresh() = with(binding) {
        networkUtils.networkDataStatus.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                refreshCatLayout.setOnRefreshListener {
                    Handler(Looper.getMainLooper()).postDelayed({
                        prepareView()
                    }, 2000L)
                }
            } else {
                snackBar(root, getString(R.string.no_inet_text)).show()
            }
        }
    }

    private fun observeNetwork() {
        networkUtils.networkDataStatus.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                prepareView()
            } else {
                snackBar(binding.root, getString(R.string.no_inet_text)).show()
            }
        }
    }

    private fun prepareNavigate(recipes: Recipes) {
        val action = CategoryFragmentDirections.actionCategoryFragmentToDetailFragment2(recipes.key, recipes.imageThumb)
        navController.navigate(action)
    }

}