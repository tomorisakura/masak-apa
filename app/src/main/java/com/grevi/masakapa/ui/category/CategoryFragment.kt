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
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.masakapa.R
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.ui.adapter.CategoryItemAdapter
import com.grevi.masakapa.ui.adapter.RecipesAdapter
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Listenear
import com.grevi.masakapa.util.Resource
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private val arg : CategoryFragmentArgs by navArgs()
    private val recipesViewModel by viewModels<RecipesViewModel>()
    private lateinit var categoryItemAdapter: CategoryItemAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("ARGS", arg.catKey.toString())
        navController = Navigation.findNavController(view)
        prepareView(view)
        swipeRefresh(view)
    }

    private fun prepareView(view: View) {
        categoryItemAdapter = CategoryItemAdapter()
        rv_recipes_category_list.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rv_recipes_category_list.adapter = categoryItemAdapter
        refresh_cat_layout.isRefreshing = true

        rv_recipes_category_list.animate().alpha(0f).duration = 1000L

        recipesViewModel.categoryResult(arg.catKey!!).observe(viewLifecycleOwner, Observer {response ->
            Log.v("RESPONSE", response.status.name)
            categoryHintText.text = arg.catName
            when(response.status) {
                Resource.Status.ERROR -> toast(view.context, "Err : ${response.msg}")
                Resource.Status.LOADING -> {
                    toast(view.context, response.msg.toString())
                    refresh_cat_layout.isRefreshing = true
                }
                Resource.Status.SUCCESS -> {
                    response.data?.results?.let {
                        categoryItemAdapter.addItem(it)
                    }
                    rv_recipes_category_list.animate().alpha(1f).duration = 1000L
                    refresh_cat_layout.isRefreshing = false
                }
            }
        })

        categoryItemAdapter.itemRecipes(object : Listenear{
            override fun onItemSelected(recipes: Recipes) {
                prepareNavigate(recipes)
            }

        })
    }

    private fun swipeRefresh(view: View) {
        refresh_cat_layout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                prepareView(view)
            }, 2000L)
        }
    }

    private fun prepareNavigate(recipes: Recipes) {
        val action = CategoryFragmentDirections.actionCategoryFragmentToDetailFragment2(recipes.key, recipes.imageThumb)
        navController.navigate(action)
    }

}