package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grevi.masakapa.R
import kotlinx.android.synthetic.main.lists_ingredients.view.*

class StepAdapte :RecyclerView.Adapter<StepAdapte.StepVH>() {

    private val steps : MutableList<String> = mutableListOf()

    inner class StepVH(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(string: String) {
            itemView.ingredientsText.text = string
        }
    }

    fun addList(list: List<String>) {
        steps.clear()
        steps.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lists_ingredients, parent, false)
        return StepVH(view)
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    override fun onBindViewHolder(holder: StepVH, position: Int) {
        holder.bind(steps[position])
    }
}