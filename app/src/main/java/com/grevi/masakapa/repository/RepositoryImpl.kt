package com.grevi.masakapa.repository

import com.grevi.masakapa.data.local.RecipesDataSource
import com.grevi.masakapa.data.local.entity.*
import com.grevi.masakapa.data.remote.ResponseMapper
import com.grevi.masakapa.data.remote.service.ApiHelper
import com.grevi.masakapa.repository.mapper.MapperEntity
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: ApiHelper,
    private val recipesDataSource: RecipesDataSource,
    private val mapper: MapperEntity,
) : Repository, ResponseMapper() {

    override suspend fun getRecipes() =
        launch(
            call = { api.getAllRecipes() },
            saveToLocal = { mapper.recipesMapper(it) }
        )

    override suspend fun getDetailRecipes(key: String) =
        launch(
            call = { api.getDetailRecipes(key) },
            saveToLocal = { mapper.detailMapper(it) }
        )

    override suspend fun getSearchRecipe(query: String) =
        launch(call = { api.getSearchRecipes(query) })

    override suspend fun getCategory() =
        launch(
            call = { api.getCategory() },
            saveToLocal = { mapper.categoryMapper(it) }
        )

    override suspend fun getCategoryRecipes(key: String) =
        launch(
            call = { api.getCategoryRecipes(key) },
            saveToLocal = { mapper.recipesMapper(it) }
        )

    /**
     *Local Recipes
     * */

    override suspend fun getFlowLocalRecipes() = recipesDataSource.getFlowRecipes()

    override suspend fun insertRecipes(recipesTable: RecipesTable) =
        recipesDataSource.insertRecipes(recipesTable)

    override suspend fun getFlowCategory() = recipesDataSource.getFlowCategory()

    override suspend fun getFlowDetail(name: String) = recipesDataSource.findDetail(name)

    /**
     * Favorite Recipes
     * */

    override suspend fun getFlowFavorite() = recipesDataSource.getFavorite()

    override suspend fun insertFavorite(favorite: RecipeFavorite) =
        recipesDataSource.insertFavorite(favorite)

    override suspend fun isFavoriteExists(key: String) = flow {
        emit(recipesDataSource.isFavoriteExists(key))
    }

    override suspend fun deleteFavorite(favorite: RecipeFavorite) =
        recipesDataSource.deleteFavorite(favorite)

    override suspend fun findLocalDetailRecipeByName(name: String) =
        recipesDataSource.findRecipeByDetailName(name)
}