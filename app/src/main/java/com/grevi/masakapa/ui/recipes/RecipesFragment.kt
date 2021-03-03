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
import com.grevi.masakapa.databinding.FragmentRecipesBinding
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.ui.adapter.RecipesAdapter
import com.grevi.masakapa.ui.search.SearchActivity
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.State
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var binding : FragmentRecipesBinding
    private lateinit var navController: NavController
    private val recipesViewModel by viewModels<RecipesViewModel>()
    private val recipesAdapter: RecipesAdapter by lazy { RecipesAdapter() }
    private val snapHelper: LinearSnapHelper by lazy { LinearSnapHelper() }

    private val TAG = RecipesFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecipesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        prepareView()
        swipeRefresh()
    }

    private fun prepareView() = with(binding) {
        rvRecipesList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvRecipesList.adapter = recipesAdapter

        snapHelper.attachToRecyclerView(rvRecipesList)
        rvRecipesList.animate().alpha(0f).duration = 1000L

        recipesViewModel.recipes.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is State.Loading -> Log.i(TAG, response.msg)
                is State.Error -> toast(requireContext(), response.msg)
                is State.Success -> {
                    refreshLayout.isRefreshing = false
                    recipesAdapter.addItem(response.data.results)
                    rvRecipesList.animate().alpha(1f).duration = 1000L
                    tvGreeting.visibility = View.VISIBLE
                    tvGreeting.animate().alpha(1f).duration = 1000L
                    recipesAdapter.itemTouch = { prepareNavigate(it) }
                }
                else -> Log.i(TAG, "")
            }

        })

        searchCard.setOnClickListener {
            Intent(activity, SearchActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun prepareNavigate(recipes : Recipes) {
        val action = RecipesFragmentDirections.actionRecipesFragmentToDetailFragment(recipes.key, recipes.imageThumb)
        navController.navigate(action)
    }

    private fun swipeRefresh() = with(binding) {
        refreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                prepareView()
            }, 2000L)
        }
    }

    private fun snackBar(view: View, msg: String) : Snackbar {
        return Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
    }
}