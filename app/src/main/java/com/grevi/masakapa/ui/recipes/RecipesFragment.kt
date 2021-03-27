package com.grevi.masakapa.ui.recipes

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.FragmentRecipesBinding
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.ui.adapter.RecipesAdapter
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.NetworkUtils
import com.grevi.masakapa.util.State
import com.grevi.masakapa.util.snackBar
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var binding : FragmentRecipesBinding
    private lateinit var navController: NavController
    private val recipesViewModel by viewModels<RecipesViewModel>()
    private val recipesAdapter: RecipesAdapter by lazy { RecipesAdapter() }
    private val snapHelper: LinearSnapHelper by lazy { LinearSnapHelper() }
    private val networkUtils : NetworkUtils by lazy { NetworkUtils(requireContext()) }

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
        setHasOptionsMenu(true)
        observeNetwork()
        swipeRefresh()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.bucket -> {
                RecipesFragmentDirections.actionRecipesFragmentToMarkFragment2().also { navDirections -> navController.navigate(navDirections) }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun prepareView() = with(binding) {
        rvRecipesList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvRecipesList.adapter = recipesAdapter

        snapHelper.attachToRecyclerView(rvRecipesList)
        rvRecipesList.animate().alpha(0f).duration = 1000L

        recipesViewModel.recipes.observe(viewLifecycleOwner, {response ->
            when(response) {
                is State.Loading -> pg.visibility = View.VISIBLE
                is State.Error -> toast(requireContext(), response.msg).show()
                is State.Success -> {
                    pg.visibility = View.GONE
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
            RecipesFragmentDirections.actionRecipesFragmentToSearchFragment2().also {
                navController.navigate(it)
            }
        }
    }

    private fun prepareNavigate(recipes : Recipes) {
        val action = RecipesFragmentDirections.actionRecipesFragmentToDetailFragment(recipes.key, recipes.imageThumb)
        navController.navigate(action)
    }

    private fun swipeRefresh() = with(binding) {
        networkUtils.networkDataStatus.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                refreshLayout.setOnRefreshListener {
                    Handler(Looper.getMainLooper()).postDelayed({
                        prepareView()
                        pg.visibility = View.GONE
                    }, 2000L)
                }
            } else {
                pg.visibility = View.VISIBLE
                snackBar(root, getString(R.string.no_inet_text)).show()
            }
        }
    }

    private fun observeNetwork() = with(binding) {
        networkUtils.networkDataStatus.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                pg.visibility = View.GONE
                prepareView()
            } else {
                pg.visibility = View.VISIBLE
                snackBar(root, getString(R.string.no_inet_text)).show()
            }
        }
    }
}