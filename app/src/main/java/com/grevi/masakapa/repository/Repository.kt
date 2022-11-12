package com.grevi.masakapa.repository

import com.grevi.masakapa.data.local.entity.*
import com.grevi.masakapa.data.remote.response.CategoryResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
import com.grevi.masakapa.common.state.State
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getRecipes() : Flow<State<RecipesResponse>>
    suspend fun getDetailRecipes(key : String) : Flow<State<DetailResponse>>
    suspend fun getSearchRecipe(query : String) : Flow<State<SearchResponse>>
    suspend fun getCategory() : Flow<State<CategoryResponse>>
    suspend fun getCategoryRecipes(key: String) : Flow<State<RecipesResponse>>
    suspend fun insertRecipes(recipesTable : RecipesTable)
    suspend fun getFlowLocalRecipes() : Flow<MutableList<RecipesTable>>
    suspend fun getFlowCategory() : Flow<MutableList<Category>>
    suspend fun getFlowDetail(name : String) : Flow<List<DetailWithIngredientsAndSteps>>
    suspend fun getFlowFavorite() : Flow<List<RecipeFavorite>>
    suspend fun insertFavorite(favorite: RecipeFavorite)
    suspend fun isFavoriteExists(key : String) : Boolean
    suspend fun deleteFavorite(favorite: RecipeFavorite)
    suspend fun findLocalDetailRecipeByName(name: String): Flow<DetailTable>
}