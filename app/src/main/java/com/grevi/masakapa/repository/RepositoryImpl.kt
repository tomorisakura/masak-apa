package com.grevi.masakapa.repository

import com.grevi.masakapa.data.local.RecipesDataSource
import com.grevi.masakapa.data.local.entity.*
import com.grevi.masakapa.data.remote.ApiResponse
import com.grevi.masakapa.data.remote.data.ApiHelper
import com.grevi.masakapa.data.remote.response.CategorysResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
import com.grevi.masakapa.repository.mapper.MapperEntity
import com.grevi.masakapa.common.state.State
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: ApiHelper,
    private val recipesDataSource: RecipesDataSource,
    private val mapper: MapperEntity,
    ) : Repository, ApiResponse() {

    override suspend fun getRecipes() : State<RecipesResponse> {
        return apiResponse { api.getAllRecipes() }.also {
            saveToLocalState(it) { recipe ->
                mapper.recipesMapper(recipe)
            }
        }
    }

    override suspend fun getDetailRecipes(key : String) : State<DetailResponse> {
        return apiResponse { api.getDetailRecipes(key) }.also {
            saveToLocalState(it) { detail ->
                mapper.detailMapper(detail)
            }
        }
    }

    override suspend fun getSearchRecipe(query : String) : State<SearchResponse> {
        return apiResponse { api.getSearchRecipes(query) }
    }

    override suspend fun getCategory() : State<CategorysResponse> {
        return apiResponse { api.getCategorys() }.also {
            saveToLocalState(it) { category ->
                mapper.categoryMapper(category)
            }
        }
    }

    override suspend fun getCategoryRecipes(key: String) : State<RecipesResponse> {
        return apiResponse { api.getCategoryRecipes(key) }.also {
            saveToLocalState(it) { recipe ->
                mapper.recipesMapper(recipe)
            }
        }
    }

    /**
     *Local Recipes
     * */

    override suspend fun getFlowLocalRecipes() : Flow<MutableList<RecipesTable>>
    = recipesDataSource.getFlowRecipes()

    override suspend fun insertRecipes(recipesTable : RecipesTable)
    = recipesDataSource.insertRecipes(recipesTable)

    override suspend fun getFlowCategory() : Flow<MutableList<Category>>
    = recipesDataSource.getFlowCategory()

    override suspend fun getFlowDetail(name: String): Flow<List<DetailWithIngredientsAndSteps>>
    = recipesDataSource.findDetail(name)

    /**
     * Favorite Recipes
     * */

    override suspend fun getFlowFavorite(): Flow<List<RecipeFavorite>>
    = recipesDataSource.getFavorite()

    override suspend fun insertFavorite(favorite: RecipeFavorite)
    = recipesDataSource.insertFavorite(favorite)

    override suspend fun isFavoriteExists(key: String): Boolean
    = recipesDataSource.isFavoriteExists(key)

    override suspend fun deleteFavorite(favorite: RecipeFavorite)
    = recipesDataSource.deleteFavorite(favorite)

    override suspend fun findLocalDetailRecipeByName(name: String): Flow<DetailTable> {
        return recipesDataSource.findRecipeByDetailName(name)
    }
}