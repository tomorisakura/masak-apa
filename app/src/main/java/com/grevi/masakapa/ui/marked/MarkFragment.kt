package com.grevi.masakapa.ui.marked

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.grevi.masakapa.databinding.FragmentMarkBinding
import com.grevi.masakapa.databinding.SnapMarkLayoutBinding
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.ui.adapter.MarkAdapter
import com.grevi.masakapa.ui.search.SearchActivity
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.util.Resource
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkFragment : Fragment() {

    private lateinit var binding : FragmentMarkBinding
    private lateinit var snapMarkBinding: SnapMarkLayoutBinding
    private val databaseViewModel : DatabaseViewModel by viewModels()
    private lateinit var markAdapter: MarkAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarkBinding.inflate(inflater)
        snapMarkBinding = SnapMarkLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        prepareView()
    }

    private fun prepareView() = with(binding) {
        markAdapter = MarkAdapter()
        rvRecipesMark.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvRecipesMark.adapter = markAdapter
        databaseViewModel.listMark.observe(viewLifecycleOwner, Observer { response ->
            when(response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        markAdapter.addItem(it)
                        prepareSwipe(it)
                    }
                    showSnap(false)
                    markAdapter.itemTouch = { prepareNavigate(it) }
                }

                Resource.Status.LOADING -> {
                    toast(requireContext(),  response.msg.toString())
                }

                Resource.Status.ERROR -> {
                    showSnap(true)
                    toast(requireContext(),  response.msg.toString())
                }
            }
        })

        databaseViewModel.state.observe(viewLifecycleOwner, Observer {
            if (it) {
                showSnap(it)
            } else {
                showSnap(it)
            }
        })
    }

    private fun prepareSwipe(recipes : MutableList<RecipesTable>) {
        val simpleTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val msg = "Resep ${recipes[position].name} dihapus"
                markAdapter.removeItem(recipes[position], position)
                databaseViewModel.deleteRecipes(recipes[position])
                materialDialog(binding.root, msg).show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvRecipesMark)
    }

    private fun prepareNavigate(recipesTable: RecipesTable) {
        val action = MarkFragmentDirections.actionMarkFragmentToDetailFragment3(recipesTable.key, recipesTable.imageThumb)
        navController.navigate(action)
    }

    private fun showSnap(state : Boolean) = with(binding) {
        when(state) {
            true -> {
                snapMark.visibility = ViewGroup.VISIBLE
                snapMark.animate().alpha(1f)
                snapMarkBinding.emptyBtn.setOnClickListener {
                    Intent(activity, SearchActivity::class.java).also {
                        startActivity(it)
                        activity?.finish()
                    }
                }
            }

            false -> {
                snapMark.visibility = ViewGroup.INVISIBLE
                snapMark.animate().alpha(0f)
            }
        }
    }

    private fun materialDialog(view: View, msg: String) : Snackbar {
        return Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
    }
}