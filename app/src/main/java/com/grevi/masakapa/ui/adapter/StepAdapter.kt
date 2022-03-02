package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.grevi.masakapa.databinding.ListsIngredientsBinding
import com.grevi.masakapa.common.differ.Differ

class StepAdapter :RecyclerView.Adapter<StepAdapter.StepVH>() {
    private val steps : MutableList<String> = ArrayList()

    inner class StepVH(private val binding : ListsIngredientsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(string: String) = with(binding) {
            ingredientsText.text = string
        }
    }

    fun addList(list: List<String>) {
        val diffCallback = Differ(steps, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        steps.clear()
        steps.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepVH {
        val view = ListsIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StepVH(view)
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    override fun onBindViewHolder(holder: StepVH, position: Int) {
        holder.bind(steps[position])
    }
}