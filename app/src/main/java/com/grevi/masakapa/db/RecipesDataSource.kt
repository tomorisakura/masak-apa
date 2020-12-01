package com.grevi.masakapa.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.grevi.masakapa.db.entity.Recipes

interface RecipesDataSource  {
    suspend fun insertRecipes(recipes: Recipes)
    suspend fun isExistRecipes(key : String) : Boolean
    suspend fun getMarkRecipes() : List<Recipes>
}