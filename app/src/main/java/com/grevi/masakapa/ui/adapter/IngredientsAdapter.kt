package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grevi.masakapa.R
import kotlinx.android.synthetic.main.lists_ingredients.view.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsVH>() {

    private val ingredient : MutableList<String> = mutableListOf()

    fun addList(list : MutableList<String>) {
        ingredient.clear()
        ingredient.addAll(list)
    }

    inner class IngredientsVH(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(name : String) {
            with(itemView) {
                this.ingredientsText.text = name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lists_ingredients, parent, false)
        return IngredientsVH(view)
    }

    override fun getItemCount(): Int {
        return ingredient.size
    }

    override fun onBindViewHolder(holder: IngredientsVH, position: Int) {
        holder.bind(ingredient[position])
    }
}