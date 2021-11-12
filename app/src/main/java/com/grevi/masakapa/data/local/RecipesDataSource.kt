package com.grevi.masakapa.data.local

import com.grevi.masakapa.data.local.entity.*
import kotlinx.coroutines.flow.Flow

interface RecipesDataSource  {
    suspend fun insertRecipes(recipesTable: RecipesTable)
    suspend fun getFlowRecipes() : Flow<MutableList<RecipesTable>>
    suspend fun insertCategory(category: Category)
    suspend fun getAllCategory() : MutableList<Category>
    suspend fun getFlowCategory() : Flow<MutableList<Category>>
    suspend fun insertDetail(detailTable: DetailTable)
    suspend fun findDetail(name: String) : Flow<List<DetailWithIngredientsAndSteps>>
    suspend fun insertFavorite(favorite: RecipeFavorite)
    suspend fun isFavoriteExists(key: String) : Boolean
    suspend fun getFavorite() : Flow<List<RecipeFavorite>>
    suspend fun deleteFavorite(favorite: RecipeFavorite)
}