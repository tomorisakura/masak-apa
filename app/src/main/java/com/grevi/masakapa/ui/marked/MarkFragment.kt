package com.grevi.masakapa.ui.marked

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grevi.masakapa.data.local.entity.RecipeFavorite
import com.grevi.masakapa.databinding.FragmentMarkBinding
import com.grevi.masakapa.ui.adapter.MarkAdapter
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.common.base.observeDataFlow
import com.grevi.masakapa.common.coroutine.coroutineJob
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.common.popup.snackBar
import com.grevi.masakapa.util.Constant.ONE_FLOAT
import com.grevi.masakapa.util.Constant.ZERO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MarkFragment : BaseFragment<FragmentMarkBinding, DatabaseViewModel>() {

    private val databaseViewModel : DatabaseViewModel by viewModels()
    private val markAdapter: MarkAdapter by lazy { MarkAdapter{ navigateToDetail(it) } }
    private val job : Job by lazy { Job() }

    override fun getViewModelClass(): Class<DatabaseViewModel> = DatabaseViewModel::class.java

    override fun getViewBindingInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMarkBinding {
        return FragmentMarkBinding.inflate(inflater, container, false)
    }

    override fun subscribeUI() {
        observeView()
        deleteRecipes()
    }

    private fun observeView() = with(binding) {
        rvRecipesMark.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false)
            adapter = markAdapter
        }
        coroutineJob(job) {
            observeDataFlow(databaseViewModel.recipesBucket) {
                markAdapter.addItem(it)
            }
        }
    }

    private fun deleteRecipes() = with(binding) {
        val simpleTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ZERO or ItemTouchHelper.RIGHT) {
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
                viewHolder.itemView.alpha = ONE_FLOAT
            }

        }
        ItemTouchHelper(simpleTouchCallback).apply {
            attachToRecyclerView(rvRecipesMark)
        }
    }

    private fun navigateToDetail(favorite: RecipeFavorite) {
        MarkFragmentDirections
            .actionMarkFragment2ToDetailFragment(favorite.key, favorite.imageThumb).also {
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