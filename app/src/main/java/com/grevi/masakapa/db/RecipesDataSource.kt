package com.grevi.masakapa.db

import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.RecipesTable
import kotlinx.coroutines.flow.Flow

interface RecipesDataSource  {
    suspend fun insertRecipes(recipesTable: RecipesTable)
    suspend fun isExistRecipes(key : String) : Boolean
    suspend fun getMarkRecipes() : MutableList<RecipesTable>
    suspend fun deleteRecipes(recipesTable: RecipesTable)
    suspend fun getFlowRecipes() : Flow<List<RecipesTable>>
    suspend fun insertCategory(category: Category)
    suspend fun getAllCategory() : MutableList<Category>
    suspend fun getFlowCategory() : Flow<MutableList<Category>>
}