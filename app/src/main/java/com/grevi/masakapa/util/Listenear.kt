package com.grevi.masakapa.util

import android.view.View
import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.model.Categorys
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.model.Search

interface Listenear {
    fun onItemSelected(recipes: Recipes)
}

interface SearchListenear {
    fun onItemSelected(search: Search)
}

interface CategoryListenear {
    fun onItemSelected(categorys: Category)
}

interface HandlerListener {
    fun message(msg : String, state : Boolean)
}

interface MarkListener {
    fun onItemSelected(recipes: com.grevi.masakapa.db.entity.Recipes)
}