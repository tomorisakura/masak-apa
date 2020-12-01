package com.grevi.masakapa.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.grevi.masakapa.db.entity.Recipes
import javax.inject.Inject

class RecipesDataSourceImpl @Inject constructor(private val recipesDAO: RecipesDAO) : RecipesDataSource {
    override suspend fun insertRecipes(recipes: Recipes) = recipesDAO.insertRecipes(recipes)

    override suspend fun isExistRecipes(key: String) : Boolean = recipesDAO.isExistsRecipes(key)
    override suspend fun getMarkRecipes() : List<Recipes> = recipesDAO.getAllMarkRecipes()
}