package com.grevi.masakapa.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.common.base.observeLiveData
import com.grevi.masakapa.common.coroutine.runTask
import com.grevi.masakapa.databinding.FragmentCategoryBinding
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.ui.adapter.CategoryItemAdapter
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Constant.ONE_FLOAT
import com.grevi.masakapa.util.Constant.ONE_SECOND
import com.grevi.masakapa.util.Constant.ZERO_FLOAT
import com.grevi.masakapa.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding, RecipesViewModel>() {

    private val arg: CategoryFragmentArgs by navArgs()
    private val categoryItemAdapter: CategoryItemAdapter by lazy {
        CategoryItemAdapter { navigateToDetail(it) }
    }

    override fun getViewModelClass(): Class<RecipesViewModel> = RecipesViewModel::class.java

    override fun getViewBindingInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryBinding {
        return FragmentCategoryBinding.inflate(inflater, container, false)
    }

    override fun subscribeUI() {
        getRecipes()
        observeRecipes()
        binding.apply {
            onSwipeRefresh(refreshCatLayout, null)
            refreshCatLayout.isRefreshing = !isLoadComplete
        }
    }

    private fun getRecipes() {
        runTask { viewModel.categoryResult(arg.catKey) }
    }

    private fun observeRecipes() = with(viewModel) {
        observeLiveData(recipesData) { observeViewState(it.results) }
    }

    private fun observeViewState(recipes: MutableList<Recipes>) = with(binding) {
        rvRecipesCategoryList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvRecipesCategoryList.adapter = categoryItemAdapter
        rvRecipesCategoryList.animate().alpha(ZERO_FLOAT).duration = ONE_SECOND

        categoryItemAdapter.addItem(recipes)
        rvRecipesCategoryList.animate().alpha(ONE_FLOAT).duration = ONE_SECOND
        refreshCatLayout.isRefreshing = false
        isLoadComplete = true
        categoryHintText.show()
        categoryHintText.text = "${arg.catName} (${recipes.size})"
    }

    private fun navigateToDetail(recipes: Recipes) {
        CategoryFragmentDirections
            .actionCategoryFragment2ToDetailFragment(recipes.key, recipes.imageThumb).also {
                navController.navigate(it)
            }
    }

}