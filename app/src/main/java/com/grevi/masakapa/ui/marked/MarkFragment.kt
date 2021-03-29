package com.grevi.masakapa.ui.marked

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grevi.masakapa.databinding.FragmentMarkBinding
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.ui.adapter.MarkAdapter
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.util.State
import com.grevi.masakapa.util.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MarkFragment : Fragment() {

    private lateinit var binding : FragmentMarkBinding
    private val databaseViewModel : DatabaseViewModel by viewModels()
    private val markAdapter: MarkAdapter by lazy { MarkAdapter() }
    private lateinit var navController: NavController
    private val job : Job by lazy { Job() }

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
        deleteRecipes()
    }

    private fun prepareView() = with(binding) {
        rvRecipesMark.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = markAdapter
        }
        CoroutineScope(job + Dispatchers.Main).launch {
            databaseViewModel.recipesBucket.collect { state ->
                when(state) {
                    is State.Loading -> snackBar(root, state.msg).show()
                    is State.Error -> snackBar(root, state.msg).show()
                    is State.Success -> {
                        markAdapter.addItem(state.data)
                        markAdapter.itemTouch = { prepareNavigate(it) }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun deleteRecipes() = with(binding) {
        val simpleTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_IDLE, 0 or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                databaseViewModel.deleteRecipes(markAdapter.deleteItem(position))
                snackBar(root, markAdapter.deleteItem(position).name).show()
                /*
                * delete array of item in recyclerView
                * */
                markAdapter.removeItem(position)
                markAdapter.notifyItemRemoved(position)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f
            }

        }
        ItemTouchHelper(simpleTouchCallback).apply {
            attachToRecyclerView(rvRecipesMark)
        }
    }

    private fun prepareNavigate(recipesTable: RecipesTable) {
        MarkFragmentDirections
            .actionMarkFragment2ToDetailFragment(recipesTable.key, recipesTable.imageThumb).also {
            navController.navigate(it)
        }
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    override fun onResume() {
        super.onResume()
        job.start()
    }
}