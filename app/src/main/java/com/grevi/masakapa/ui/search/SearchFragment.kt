package com.grevi.masakapa.ui.search

import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.masakapa.R
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.common.base.observeDataFlow
import com.grevi.masakapa.common.base.observeLiveData
import com.grevi.masakapa.common.base.showSoftKey
import com.grevi.masakapa.common.coroutine.coroutineJob
import com.grevi.masakapa.common.popup.snackBar
import com.grevi.masakapa.data.local.entity.Category
import com.grevi.masakapa.databinding.FragmentSearchBinding
import com.grevi.masakapa.databinding.SnapLayoutBinding
import com.grevi.masakapa.model.Categorys
import com.grevi.masakapa.model.Search
import com.grevi.masakapa.ui.adapter.CategoryAdapter
import com.grevi.masakapa.ui.adapter.SearchAdapter
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Constant.ONE_FLOAT
import com.grevi.masakapa.util.Constant.ONE_SECOND
import com.grevi.masakapa.util.Constant.THREE
import com.grevi.masakapa.util.Constant.TWO_SECOND
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, RecipesViewModel>() {

    private lateinit var snapBinding: SnapLayoutBinding
    private val searchAdapter: SearchAdapter by lazy { SearchAdapter { navigateToDetail(it) } }
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter { navigateToCategory(it) } }

    override fun getViewModelClass(): Class<RecipesViewModel> = RecipesViewModel::class.java

    override fun getViewBindingInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        snapBinding = SnapLayoutBinding.inflate(inflater)
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun subscribeUI() {
        snapBinding.root.animate().alpha(ONE_FLOAT)
        checkPermission()
        getCategory()
        observeOnSearch()
    }

    private fun getCategory() = coroutineJob { viewModels.getCategory() }

    private fun observeOnSearch() = with(binding) {
        showSoftKey(textInputSearch, true)
        textInputSearch.setOnKeyListener { v, keyCode, event ->
            val ed = binding.textInputSearch.editableText.toString()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                observeView(ed)
                showSoftKey(v, false)
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun checkPermission() {
        if (viewModels.getStoragePermission()) {
            observeCategory()
        } else {
            snackBar(binding.root, getString(R.string.category_permission_text))
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.bucket)?.isVisible = false
    }

    private fun observeView(query: String) = with(binding) {
        observeLiveData(viewModels.recipesSearch) {
            if (it.results.isNotEmpty()) {
                val text = getString(R.string.search_success_text)
                val combine = "$text $query (${it.results.size})"
                searchResultHintText.text = combine
                searchResultHintText.animate().alpha(1f)
                snackBar(root, combine)
                observeSearchRecyclerView(it.results)
            } else {
                val text = resources.getString(R.string.failed_found_text)
                val failText = StringBuilder()
                failText.append(text).append(" $query")
                snackBar(root, failText.toString())
            }
        }
    }

    private fun observeSearchRecyclerView(search: MutableList<Search>) = with(binding) {
        rvSearchList.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
            searchAdapter.addItem(search)
            this.animate().alpha(ONE_FLOAT).duration = TWO_SECOND
        }
    }

    private fun observeCategory() = with(binding) {
        lifecycleScope.launchWhenCreated {
            observeDataFlow(viewModels.category) {
                categorysHintLabel.animate().alpha(ONE_FLOAT)
                rvCategorysList.apply {
                    this.animate().alpha(ONE_FLOAT)
                    layoutManager = GridLayoutManager(context, THREE)
                    adapter = categoryAdapter
                    categoryAdapter.addItem(it.results)
                    this.animate().alpha(ONE_FLOAT).duration = TWO_SECOND
                }
                categorysHintLabel.animate().alpha(ONE_FLOAT).duration = ONE_SECOND
                binding.pgMainCategory.visibility = View.GONE
            }
        }
    }

    private fun navigateToDetail(search: Search) {
        SearchFragmentDirections.actionSearchFragment2ToDetailFragment(
            search.key,
            search.imageThumb
        ).also {
            navController.navigate(it)
        }
    }

    private fun navigateToCategory(categorys: Categorys) {
        SearchFragmentDirections.actionSearchFragment2ToCategoryFragment2(
            categorys.key,
            categorys.category
        ).also {
            navController.navigate(it)
        }
    }

}