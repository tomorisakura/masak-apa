package com.grevi.masakapa.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.FragmentSearchBinding
import com.grevi.masakapa.databinding.SnapLayoutBinding
import com.grevi.masakapa.data.local.entity.Category
import com.grevi.masakapa.model.Search
import com.grevi.masakapa.ui.adapter.CategorysAdapter
import com.grevi.masakapa.ui.adapter.SearchAdapter
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Constant.PERMISSIONS_STORAGE
import com.grevi.masakapa.util.NetworkUtils
import com.grevi.masakapa.util.State
import com.grevi.masakapa.util.snackBar
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var snapBinding : SnapLayoutBinding
    private val recipesViewModel : RecipesViewModel by viewModels()
    private lateinit var navController: NavController
    private val searchAdapter: SearchAdapter by lazy { SearchAdapter() }
    private val categoryAdapter: CategorysAdapter by lazy { CategorysAdapter() }
    private val networkUtils : NetworkUtils by lazy { NetworkUtils(requireContext()) }

    private val TAG = SearchFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        snapBinding = SnapLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setHasOptionsMenu(true)
        snapBinding.root.animate().alpha(1f)
        showSoftKey(binding.textInputSearch, true)
        networkUtils.networkDataStatus.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                binding.textInputSearch.setOnKeyListener { v, keyCode, event ->
                    val ed = binding.textInputSearch.editableText.toString()
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        Log.i(TAG, ed)
                        prepareView(ed)
                        showSoftKey(v, false)
                        return@setOnKeyListener true
                    }
                    false
                }
                if (getSharedPermission) {
                    prepareCategory()
                } else {
                    snackBar(binding.root, getString(R.string.category_permission_text)).show()
                }
            } else {
                snackBar(binding.root, getString(R.string.no_inet_text)).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.bucket)?.isVisible = false
    }

    private fun showSoftKey(view: View, state : Boolean) {
        when(state) {
            true -> {
                view.requestFocus().run {
                    val input = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    input.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                }
            }

            false -> {
                view.requestFocus().run {
                    val input = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    input.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
    }

    private fun prepareView(query : String) = with(binding) {
        recipesViewModel.searchRecipe(query).observe(viewLifecycleOwner, {results ->
            when (results) {
                is State.Loading -> Log.i(TAG, results.msg)
                is State.Error -> toast(requireContext(), results.msg)
                is State.Success -> {
                    results.data.let {
                        if (!it.results.isNullOrEmpty()) {
                            val text = getString(R.string.search_success_text)
                            val combine = "$text $query (${it.results.size})"
                            searchResultHintText.text = combine
                            searchResultHintText.animate().alpha(1f)
                            snackBar(root, combine)
                            prepareRV(it.results)
                        } else {
                            val text = resources.getString(R.string.failed_found_text)
                            val failText = StringBuilder()
                            failText.append(text).append(" $query")
                            snackBar(root, failText.toString())
                        }
                    }
                }
                else -> Log.i(TAG, "")
            }
        })

    }

    private fun prepareRV(search : MutableList<Search>) = with(binding) {
        rvSearchList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
            searchAdapter.addItem(search)
            searchAdapter.notifyDataSetChanged()
            searchAdapter.itemTouch = { prepareNavigate(it) }
            this.animate().alpha(1f).duration = 2000L
        }
    }

    private fun prepareCategory() {
        lifecycleScope.launchWhenCreated{
            recipesViewModel.category.collect { state ->
                when(state) {
                    is State.Loading -> toast(requireContext(), state.msg).show()
                    is State.Error -> toast(requireContext(), state.msg).show()
                    is State.Success -> {
                        prepareCategoryRV(state.data)
                        binding.pgMainCategory.visibility = View.GONE
                    }
                    else -> Log.i(TAG, "")
                }
            }
        }
    }

    private fun prepareCategoryRV(categorys : MutableList<Category>) = with(binding) {
        categorysHintLabel.animate().alpha(0f)
        rvCategorysList.apply {
            this.animate().alpha(0f)
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryAdapter
            categoryAdapter.addItem(categorys)
            this.animate().alpha(1f).duration = 2000L
        }
        categorysHintLabel.animate().alpha(1f).duration = 1000L
        categoryAdapter.itemTouch = { prepareNavigateCategory(it) }
    }

    private fun prepareNavigate(search: Search) {
        SearchFragmentDirections.actionSearchFragment2ToDetailFragment(search.key, search.imageThumb).also {
            navController.navigate(it)
        }
    }

    private fun prepareNavigateCategory(categorys: Category) {
        SearchFragmentDirections.actionSearchFragment2ToCategoryFragment2(categorys.key, categorys.category).also {
            navController.navigate(it)
        }
    }

    private val getSharedPermission by lazy {
        requireContext().getSharedPreferences(PERMISSIONS_STORAGE, Context.MODE_PRIVATE)
            .getBoolean(PERMISSIONS_STORAGE, false)
    }

}