package com.grevi.masakapa.ui.marked

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.masakapa.R
import com.grevi.masakapa.db.entity.Recipes
import com.grevi.masakapa.ui.adapter.MarkAdapter
import com.grevi.masakapa.ui.search.SearchActivity
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.util.MarkListener
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
        databaseViewModel.lisMark.observe(viewLifecycleOwner, Observer {
            rv_recipes_mark.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            rv_recipes_mark.adapter = markAdapter
            if (!it.isNullOrEmpty()) {
                markAdapter.addItem(it)
                showSnap(false)
            } else {
                showSnap(true)
            }
        })

        markAdapter.itemRecipes(object : MarkListener {
            override fun onItemSelected(recipes: Recipes) {
                prepareNavigate(recipes)
            }

        })
    }

    private fun prepareNavigate(recipes: Recipes) {
        val action = MarkFragmentDirections.actionMarkFragmentToDetailFragment3(recipes.key, recipes.imageThumb)
        navController.navigate(action)
    }

    private fun showSnap(state : Boolean) {
        when(state) {
            true -> {
                snapMark.animate().alpha(1f)
                emptyBtn.setOnClickListener {
                    val intent = Intent(activity, SearchActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }

            false -> {
                snapMark.visibility = ViewGroup.GONE
                snapMark.animate().alpha(0f)
            }
        }
    }
}