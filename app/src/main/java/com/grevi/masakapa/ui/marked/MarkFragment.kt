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
import com.grevi.masakapa.R
import com.grevi.masakapa.db.entity.Recipes
import com.grevi.masakapa.ui.adapter.MarkAdapter
import com.grevi.masakapa.ui.search.SearchActivity
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.util.MarkListener
import com.grevi.masakapa.util.Resource
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_mark.*
import kotlinx.android.synthetic.main.snap_mark_layout.*

@AndroidEntryPoint
class MarkFragment : Fragment() {

    private val databaseViewModel : DatabaseViewModel by viewModels()
    private lateinit var markAdapter: MarkAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        prepareRV()
    }

    private fun prepareRV() {
        markAdapter = MarkAdapter()
        rv_recipes_mark.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rv_recipes_mark.adapter = markAdapter
        databaseViewModel.listMark.observe(viewLifecycleOwner, Observer { response ->
            when(response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        markAdapter.addItem(it)
                        prepareSwipe(it)
                    }
                    showSnap(false)
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

        markAdapter.itemRecipes(object : MarkListener {
            override fun onItemSelected(recipes: Recipes) {
                prepareNavigate(recipes)
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

    private fun prepareSwipe(recipes : List<Recipes>) {
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
                databaseViewModel.deleteRecipes(recipes[position], position)
                materialDialog(mainMark, msg).show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleTouchCallback)
        itemTouchHelper.attachToRecyclerView(rv_recipes_mark)
    }

    private fun prepareNavigate(recipes: Recipes) {
        val action = MarkFragmentDirections.actionMarkFragmentToDetailFragment3(recipes.key, recipes.imageThumb)
        navController.navigate(action)
    }

    private fun showSnap(state : Boolean) {
        when(state) {
            true -> {
                snapMark.visibility = ViewGroup.VISIBLE
                snapMark.animate().alpha(1f)
                emptyBtn.setOnClickListener {
                    val intent = Intent(activity, SearchActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
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