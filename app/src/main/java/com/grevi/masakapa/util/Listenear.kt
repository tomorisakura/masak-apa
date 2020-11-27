package com.grevi.masakapa.util

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
    fun onItemSelected(categorys: Categorys)
}