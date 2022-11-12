package com.grevi.masakapa.ui.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.common.base.observeDataFlow
import com.grevi.masakapa.common.coroutine.runTask
import com.grevi.masakapa.databinding.FragmentRecipesBinding
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.ui.adapter.RecipesAdapter
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Constant.ONE_SECOND
import com.grevi.masakapa.util.Constant.ZERO_FLOAT
import com.grevi.masakapa.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : BaseFragment<FragmentRecipesBinding, RecipesViewModel>() {

    private val recipesAdapter: RecipesAdapter by lazy {
        RecipesAdapter { navigateToDetail(it) }
    }

    override fun getViewModelClass(): Class<RecipesViewModel> = RecipesViewModel::class.java

    override fun getViewBindingInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecipesBinding {
        return FragmentRecipesBinding.inflate(inflater, container, false)
    }

    override fun subscribeUI() {
        initView()
        getRecipes()
        observeRecipes()
        binding.apply { onSwipeRefresh(refreshLayout, pg) }
    }

    private fun getRecipes() = runTask {
        viewModel.getRecipes()
    }

    private fun initView() = with(binding) {
        rvRecipesList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        rvRecipesList.adapter = recipesAdapter
        snapHelper.attachToRecyclerView(rvRecipesList)
        rvRecipesList.animate().alpha(ZERO_FLOAT).duration = ONE_SECOND
        refreshLayout.isRefreshing = true
    }

    private fun observeRecipes() = with(viewModel) {
        observeDataFlow(recipesLimit) {
            observeViewState()
            recipesAdapter.addItem(it.results)
        }
    }

    private fun observeViewState() = with(binding) {
        pg.visibility = View.GONE
        refreshLayout.isRefreshing = false
        rvRecipesList.show()
        tvGreeting.show()
    }

    private fun navigateToDetail(recipes: Recipes) {
        RecipesFragmentDirections
            .actionRecipesFragmentToDetailFragment(recipes.key, recipes.imageThumb).also {
                navController.navigate(it)
            }
    }
}