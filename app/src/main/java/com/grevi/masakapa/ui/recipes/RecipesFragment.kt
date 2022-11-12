package com.grevi.masakapa.ui.recipes

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.masakapa.R
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.common.base.observeDataFlow
import com.grevi.masakapa.common.coroutine.coroutineJob
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
        RecipesAdapter{ navigateToDetail(it) }
    }

    override fun getViewModelClass(): Class<RecipesViewModel> = RecipesViewModel::class.java

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.bucket -> {
                RecipesFragmentDirections.actionRecipesFragmentToMarkFragment2().also {
                        navDirections -> navController.navigate(navDirections)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getViewBindingInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecipesBinding {
        return FragmentRecipesBinding.inflate(inflater, container, false)
    }

    override fun subscribeUI() {
        observeView()
        getRecipes()
        observeRecipes()
        binding.apply { onSwipeRefresh(refreshLayout, pg) }
    }

    private fun getRecipes() = coroutineJob {
        viewModels.getRecipes()
    }

    private fun observeView() = with(binding) {
        rvRecipesList.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false
        )
        rvRecipesList.adapter = recipesAdapter
        snapHelper.attachToRecyclerView(rvRecipesList)
        rvRecipesList.animate().alpha(ZERO_FLOAT).duration = ONE_SECOND
    }

    private fun observeRecipes() = with(viewModels) {
        observeDataFlow(recipesLimit) { recipes ->
            observeViewState()
            recipesAdapter.addItem(recipes.results)
        }
    }

    private fun observeViewState() = with(binding) {
        pg.visibility = View.GONE
        refreshLayout.isRefreshing = false
        rvRecipesList.show()
        tvGreeting.show()
    }

    private fun navigateToDetail(recipes : Recipes) {
        RecipesFragmentDirections
            .actionRecipesFragmentToDetailFragment(recipes.key, recipes.imageThumb).also {
                navController.navigate(it)
            }
    }
}