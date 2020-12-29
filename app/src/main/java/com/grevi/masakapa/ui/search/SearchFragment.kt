package com.grevi.masakapa.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.grevi.masakapa.R
import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.model.Categorys
import com.grevi.masakapa.model.Search
import com.grevi.masakapa.ui.adapter.CategorysAdapter
import com.grevi.masakapa.ui.adapter.SearchAdapter
import com.grevi.masakapa.ui.recipes.RecipesFragmentDirections
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.CategoryListenear
import com.grevi.masakapa.util.Resource
import com.grevi.masakapa.util.SearchListenear
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import java.lang.StringBuilder


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val recipesViewModel : RecipesViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var navController: NavController
    private lateinit var categorysAdapter: CategorysAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        searchAdapter = SearchAdapter()
        prepareCategory(view)
        snapLayout.animate().alpha(1f)
        showSoftKey(textInputSearch, true)
        textInputSearch.setOnKeyListener { v, keyCode, event ->
            val ed = textInputSearch.editableText.toString()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                Log.v("PRESSED", ed)
                prepareRV(query = ed, view = view)
                showSoftKey(v, false)
                return@setOnKeyListener true
            }
            false
        }
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

    private fun prepareRV(query : String, view: View) {
        recipesViewModel.searchRecipe(query).observe(viewLifecycleOwner, Observer {results ->
            Log.v("RECIPES_SEARCH_RES", results.status.toString())
            when (results.status) {
                Resource.Status.LOADING -> Log.v("SEARCH_LOAD", "loading...")
                Resource.Status.ERROR -> toast(view.context, results.msg.toString())
                Resource.Status.SUCCESS -> {
                    results.data?.let{
                        if (!it.results.isNullOrEmpty()) {
                            val text = getString(R.string.search_success_text)
                            val combine = "$text $query (${it.results.size})"
                            searchResultHintText.text = combine
                            searchResultHintText.animate().alpha(1f)
                            snackBar(view, combine)
                            prepareRV(it.results)
                        } else {
                            val text = resources.getString(R.string.failed_found_text)
                            val failText = StringBuilder()
                            failText.append(text).append(" $query")
                            snackBar(view, failText.toString())
                        }
                    }
                }
            }
        })

        searchAdapter.itemRecipes(object : SearchListenear {
            override fun onItemSelected(search: Search) {
                prepareNavigate(search)
            }

        })
    }

    private fun prepareRV(search : MutableList<Search>) {
        rv_search_list.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rv_search_list.adapter = searchAdapter
        searchAdapter.addItem(search)
        searchAdapter.notifyDataSetChanged()
        rv_search_list.animate().alpha(1f).duration = 2000L
    }

    private fun prepareCategory(view: View) {
        recipesViewModel.categoryFlow.observe(viewLifecycleOwner, Observer { results ->
            when (results.status) {
                Resource.Status.LOADING -> Log.v("SEARCH_LOAD", "loading...")
                Resource.Status.ERROR -> toast(view.context, results.msg.toString())
                Resource.Status.SUCCESS -> {
                    results.data?.let {
                        prepareCategoryRV(view, it)
                    }
                    pgMainCategory.visibility = View.GONE
                }
            }
        })
    }

    private fun prepareCategoryRV(view: View, categorys : MutableList<Category>) {
        rv_categorys_list.animate().alpha(0f)
        categorysHintLabel.animate().alpha(0f)
        categorysAdapter = CategorysAdapter()
        rv_categorys_list.layoutManager = GridLayoutManager(view.context, 3)
        rv_categorys_list.adapter = categorysAdapter
        categorysAdapter.addItem(categorys)
        categorysAdapter.notifyDataSetChanged()
        categorysHintLabel.animate().alpha(1f).duration = 1000L
        rv_categorys_list.animate().alpha(1f).duration = 2000L

        categorysAdapter.itemCategory(object : CategoryListenear {
            override fun onItemSelected(categorys: Category) {
                prepareNavigateCategory(categorys)
            }

        })
    }

    private fun prepareNavigate(search: Search) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(search.key, search.imageThumb)
        navController.navigate(action)
    }

    private fun prepareNavigateCategory(categorys: Category) {
        val action = SearchFragmentDirections.actionSearchFragmentToCategoryFragment(categorys.key, categorys.category)
        navController.navigate(action)
    }

    private fun snackBar(view: View, msg : String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }


}