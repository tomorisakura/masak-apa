package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.grevi.masakapa.databinding.ListsIngredientsBinding
import com.grevi.masakapa.common.differ.Differ

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsVH>() {
    private val ingredient : MutableList<String> = ArrayList()

    fun addList(list : MutableList<String>) {
        val diffCallback = Differ(ingredient, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        ingredient.clear()
        ingredient.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class IngredientsVH(private val binding : ListsIngredientsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name : String) = with(binding) {
            ingredientsText.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsVH {
        val view = ListsIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientsVH(view)
    }

    override fun getItemCount(): Int {
        return ingredient.size
    }

    override fun onBindViewHolder(holder: IngredientsVH, position: Int) {
        holder.bind(ingredient[position])
    }
}