package com.grevi.masakapa.ui.recipes

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.masakapa.R
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.ui.adapter.RecipesAdapter
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Listenear
import com.grevi.masakapa.util.Resource.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.*

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val recipesViewModel by viewModels<RecipesViewModel>()
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
        navController = Navigation.findNavController(view)
        recipesAdapter = RecipesAdapter()
        prepareView()
        swipeRefresh()
    }

    private fun prepareView() {

        rv_recipes_list.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rv_recipes_list.adapter = recipesAdapter
        refresh_layout.isRefreshing = true

        try {
            recipesViewModel.recipes.observe(viewLifecycleOwner, Observer {response ->
                when(response.status) {
                    Status.SUCCESS -> {
                        response.data?.results?.let {
                            recipesAdapter.addItem(it)
                            recipesAdapter.notifyDataSetChanged()
                        }

                        refresh_layout.isRefreshing = false
                    }

                    Status.LOADING -> {
                        refresh_layout.isRefreshing = true
                    }

                    Status.ERROR -> {
                        Toast.makeText(this.context, response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                recipesAdapter.itemRecipes(object : Listenear{
                    override fun onItemSelected(recipes: Recipes) {
                        prepareNavigate(recipes)
                    }

                })
            })
        } catch (e : Exception) {
            Log.e("P_VIEW", e.toString())
        }
    }

    private fun prepareNavigate(recipes : Recipes) {
        val action = RecipesFragmentDirections.actionRecipesFragmentToDetailFragment(recipes.key, recipes.imageThumb)
        navController.navigate(action)
    }

    private fun swipeRefresh() {
        refresh_layout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                prepareView()
            }, 2000L)
        }
    }
}