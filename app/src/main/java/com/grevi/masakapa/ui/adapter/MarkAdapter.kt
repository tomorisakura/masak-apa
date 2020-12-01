package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.masakapa.R
import com.grevi.masakapa.db.entity.Recipes
import com.grevi.masakapa.util.MarkListener
import kotlinx.android.synthetic.main.lists_recipes.view.*

class MarkAdapter : RecyclerView.Adapter<MarkAdapter.MarkVH>() {

    private val recipes : MutableList<Recipes> = mutableListOf()
    private var listenear : MarkListener? = null

    inner class MarkVH(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(recipes : Recipes) {
            with(itemView) {
                Glide.with(this.context).load(recipes.imageThumb).placeholder(R.drawable.placeholder).into(imgThumb)
                recipesTitle.text = recipes.name
                dificultyText.text = recipes.dificulty
                portionText.text = recipes.portion
                timesText.text = recipes.times
                this.setOnClickListener { listenear?.onItemSelected(recipes) }
            }
        }
    }

    internal fun itemRecipes(listenear: MarkListener) {
        this.listenear = listenear
    }

    fun addItem(item : List<Recipes>) {
        notifyDataSetChanged()
        recipes.clear()
        recipes.addAll(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lists_recipes, parent, false)
        return MarkVH(view)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: MarkVH, position: Int) {
        holder.bind(recipes[position])
    }

}