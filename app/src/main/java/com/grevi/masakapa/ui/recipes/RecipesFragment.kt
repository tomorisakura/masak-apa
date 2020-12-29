package com.grevi.masakapa.ui.recipes

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.snackbar.Snackbar
import com.grevi.masakapa.R
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.ui.adapter.RecipesAdapter
import com.grevi.masakapa.ui.search.SearchActivity
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
    private lateinit var snapHelper: LinearSnapHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        recipesAdapter = RecipesAdapter()
        snapHelper = LinearSnapHelper()
        prepareView(view)
        swipeRefresh(view)
    }

    private fun prepareView(view: View) {
        rv_recipes_list.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rv_recipes_list.adapter = recipesAdapter
        //refresh_layout.isRefreshing = true
        showInetSnap(false, view)
        snapHelper.attachToRecyclerView(rv_recipes_list)
        rv_recipes_list.animate().alpha(0f).duration = 1000L

        recipesViewModel.recipes.observe(viewLifecycleOwner, Observer {response ->
            Log.v("RESPONSE", response.status.name)
            when(response.status) {
                Status.ERROR -> {
                    snackBar(view, getString(R.string.no_inet_text)).show()
                    showInetSnap(true, view)
                }
                Status.LOADING -> {
                    snackBar(view, response.msg.toString()).show()
                    refresh_layout.isRefreshing = true
                }
                Status.SUCCESS -> {
                    response.data?.results?.let {
                        recipesAdapter.addItem(it)
                    }
                    rv_recipes_list.animate().alpha(1f).duration = 1000L
                    refresh_layout.isRefreshing = false
                    showInetSnap(false, view)
                    tv_greeting.visibility = View.VISIBLE
                    tv_greeting.animate().alpha(1f).duration = 1000L
                }
            }

            recipesAdapter.itemRecipes(object : Listenear{
                override fun onItemSelected(recipes: Recipes) {
                    prepareNavigate(recipes)
                }

            })
        })

        search_card.setOnClickListener {
            val intent = Intent(view.context, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun prepareNavigate(recipes : Recipes) {
        val action = RecipesFragmentDirections.actionRecipesFragmentToDetailFragment(recipes.key, recipes.imageThumb)
        navController.navigate(action)
    }

    private fun swipeRefresh(view: View) {
        refresh_layout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                prepareView(view)
            }, 2000L)
        }
    }

    private fun snackBar(view: View, msg: String) : Snackbar {
        return Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
    }

    private fun showInetSnap(state : Boolean, view: View) {
        when(state) {
            true -> {
                snap_no_inet.visibility = View.VISIBLE
                snap_no_inet.animate().alpha(1f)
            }

            false -> {
                snap_no_inet.visibility = View.INVISIBLE
                tv_greeting.visibility = View.INVISIBLE
                snap_no_inet.animate().alpha(0f)
                tv_greeting.animate().alpha(0f)
            }
        }
    }
}