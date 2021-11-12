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
import com.grevi.masakapa.util.ResponseException
import com.grevi.masakapa.util.State
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiHelper: ApiHelper,
                                         private val recipesDataSource: RecipesDataSource,
                                         private val mapper: MapperEntity) : Repository, ApiResponse() {

    override suspend fun getRecipes() : State<RecipesResponse> {
        return apiResponse { apiHelper.getAllRecipes() }.also { state ->
            when(state) {
                is State.Error -> throw  ResponseException(state.msg)
                is State.Success -> {
                    state.data.results.map { recipes ->
                        mapper.recipesMapper(recipes).let {
                            recipesDataSource.insertRecipes(it)
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    override suspend fun getDetailRecipes(key : String) : State<DetailResponse> {
        return apiResponse { apiHelper.getDetailRecipes(key) }.also { state ->
            when(state) {
                is State.Error -> throw ResponseException(state.msg)
                is State.Success -> {
                    state.data.let {
                        mapper.detailMapper(it.results).let { detail ->
                            recipesDataSource.insertDetail(detail)
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    override suspend fun getSearchRecipe(query : String) : State<SearchResponse> {
        return apiResponse { apiHelper.getSearchRecipes(query) }
    }

    override suspend fun getCategory() : State<CategorysResponse> {
        return apiResponse { apiHelper.getCategorys() }.also {
            when(it) {
                is State.Error -> throw ResponseException(it.msg)
                is State.Success -> {
                    it.data.results.map { result ->
                        mapper.categoryMapper(result).let { category ->
                            recipesDataSource.insertCategory(category)
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    override suspend fun getCategoryRecipes(key: String) : State<RecipesResponse> {
        return apiResponse { apiHelper.getCategoryRecipes(key) }
    }

    /**
     *Local Recipes
     * */

    override suspend fun getFlowLocalRecipes() : Flow<MutableList<RecipesTable>> = recipesDataSource.getFlowRecipes()
    override suspend fun insertRecipes(recipesTable : RecipesTable) = recipesDataSource.insertRecipes(recipesTable)

    override suspend fun getFlowCategory() : Flow<MutableList<Category>> = recipesDataSource.getFlowCategory()
    override suspend fun getFlowDetail(name: String): Flow<List<DetailWithIngredientsAndSteps>> = recipesDataSource.findDetail(name)

    /**
     * Favorite Recipes
     * */

    override suspend fun getFlowFavorite(): Flow<List<RecipeFavorite>> = recipesDataSource.getFavorite()
    override suspend fun insertFavorite(favorite: RecipeFavorite) = recipesDataSource.insertFavorite(favorite)
    override suspend fun isFavoriteExists(key: String): Boolean = recipesDataSource.isFavoriteExists(key)
    override suspend fun deleteFavorite(favorite: RecipeFavorite) = recipesDataSource.deleteFavorite(favorite)
}