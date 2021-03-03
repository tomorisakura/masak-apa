package com.grevi.masakapa.ui.marked

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.grevi.masakapa.databinding.FragmentMarkBinding
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.ui.adapter.MarkAdapter
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.util.State
import com.grevi.masakapa.util.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MarkFragment : Fragment() {

    private lateinit var binding : FragmentMarkBinding
    private val databaseViewModel : DatabaseViewModel by viewModels()
    private val markAdapter: MarkAdapter by lazy { MarkAdapter() }
    private val job : Job by lazy { Job() }
    private lateinit var navController: NavController

    private val TAG = MarkFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarkBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        prepareView()
    }

    private fun prepareView() = with(binding) {
        CoroutineScope(Dispatchers.Main + job).launch {
            rvRecipesMark.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = markAdapter
            }
            databaseViewModel.listMark.collect { state ->
                when(state) {
                    is State.Loading -> snackBar(root, state.msg).show()
                    is State.Error -> snackBar(root, state.msg).show()
                    is State.Success -> {
                        markAdapter.addItem(state.data)
                        markAdapter.itemTouch = { prepareNavigate(it) }
                        prepareSwipe(state.data)
                    }
                    else -> Log.i(TAG, "")
                }
            }
        }
    }

    private fun prepareSwipe(recipes : MutableList<RecipesTable>) {
        val simpleTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                markAdapter.clearItem(recipes)
                databaseViewModel.deleteRecipes(recipes[position])
                val msg = "Resep ${recipes[position].name} dihapus"
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

    private fun materialDialog(view: View, msg: String) : Snackbar {
        return Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
    }
}