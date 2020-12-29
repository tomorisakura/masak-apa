package com.grevi.masakapa.db

import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.Recipes
import kotlinx.coroutines.flow.Flow

interface RecipesDataSource  {
    suspend fun insertRecipes(recipes: Recipes)
    suspend fun isExistRecipes(key : String) : Boolean
    suspend fun getMarkRecipes() : MutableList<Recipes>
    suspend fun deleteRecipes(recipes: Recipes)
    suspend fun getFlowRecipes() : Flow<List<Recipes>>
    suspend fun insertCategory(category: Category)
    suspend fun getAllCategory() : MutableList<Category>
    suspend fun getFlowCategory() : Flow<MutableList<Category>>
}