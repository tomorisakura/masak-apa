package com.grevi.masakapa.repository

import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.network.response.CategorysResponse
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.network.response.SearchResponse
import com.grevi.masakapa.util.State
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getRecipes() : State<RecipesResponse>
    suspend fun getDetailRecipes(key : String) : State<DetailResponse>
    suspend fun getSearchRecipe(query : String) : State<SearchResponse>
    suspend fun getCategory() : State<CategorysResponse>
    suspend fun getCategoryRecipes(key: String) : State<RecipesResponse>
    suspend fun insertRecipes(recipesTable : RecipesTable)
    suspend fun isExistRecipes(key : String) : Boolean
    suspend fun getMarkedRecipes() : MutableList<RecipesTable>
    suspend fun deleteRecipes(recipesTable: RecipesTable)
    suspend fun getFlowRecipes() : Flow<List<RecipesTable>>
    suspend fun getFlowCategory() : Flow<MutableList<Category>>
}