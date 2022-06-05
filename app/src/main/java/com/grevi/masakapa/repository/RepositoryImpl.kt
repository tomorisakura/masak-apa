package com.grevi.masakapa.repository

import com.grevi.masakapa.common.state.State
import com.grevi.masakapa.data.local.RecipesDataSource
import com.grevi.masakapa.data.local.entity.*
import com.grevi.masakapa.data.remote.ResponseMapper
import com.grevi.masakapa.data.remote.data.ApiHelper
import com.grevi.masakapa.data.remote.response.CategoryResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
import com.grevi.masakapa.repository.mapper.MapperEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: ApiHelper,
    private val recipesDataSource: RecipesDataSource,
    private val mapper: MapperEntity,
) : Repository, ResponseMapper() {

    override suspend fun getRecipes(): Flow<State<RecipesResponse>> {
        return flow {
            emit(
                launch(
                    call = { api.getAllRecipes() },
                    saveToLocal = { mapper.recipesMapper(it) }
                )
            )
        }
    }

    override suspend fun getDetailRecipes(key: String): State<DetailResponse> {
        return launch(
            call = { api.getDetailRecipes(key) },
            saveToLocal = { mapper.detailMapper(it) }
        )
    }

    override suspend fun getSearchRecipe(query: String): State<SearchResponse> {
        return launch(call = { api.getSearchRecipes(query) })
    }

    override suspend fun getCategory(): State<CategoryResponse> {
        return launch(
            call = { api.getCategory() },
            saveToLocal = { mapper.categoryMapper(it) }
        )
    }

    override suspend fun getCategoryRecipes(key: String): State<RecipesResponse> {
        return launch(
            call = { api.getCategoryRecipes(key) },
            saveToLocal = { mapper.recipesMapper(it) }
        )
    }

    /**
     *Local Recipes
     * */

    override suspend fun getFlowLocalRecipes(): Flow<MutableList<RecipesTable>> =
        recipesDataSource.getFlowRecipes()

    override suspend fun insertRecipes(recipesTable: RecipesTable) =
        recipesDataSource.insertRecipes(recipesTable)

    override suspend fun getFlowCategory(): Flow<MutableList<Category>> =
        recipesDataSource.getFlowCategory()

    override suspend fun getFlowDetail(name: String): Flow<List<DetailWithIngredientsAndSteps>> =
        recipesDataSource.findDetail(name)

    /**
     * Favorite Recipes
     * */

    override suspend fun getFlowFavorite(): Flow<List<RecipeFavorite>> =
        recipesDataSource.getFavorite()

    override suspend fun insertFavorite(favorite: RecipeFavorite) =
        recipesDataSource.insertFavorite(favorite)

    override suspend fun isFavoriteExists(key: String): Boolean =
        recipesDataSource.isFavoriteExists(key)

    override suspend fun deleteFavorite(favorite: RecipeFavorite) =
        recipesDataSource.deleteFavorite(favorite)

    override suspend fun findLocalDetailRecipeByName(name: String): Flow<DetailTable> {
        return recipesDataSource.findRecipeByDetailName(name)
    }
}