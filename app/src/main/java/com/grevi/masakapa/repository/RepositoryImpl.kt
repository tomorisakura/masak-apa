package com.grevi.masakapa.repository

import com.grevi.masakapa.db.RecipesDataSource
import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.network.ApiResponse
import com.grevi.masakapa.network.data.ApiHelper
import com.grevi.masakapa.network.response.CategorysResponse
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.network.response.SearchResponse
import com.grevi.masakapa.repository.mapper.MapperImpl
import com.grevi.masakapa.util.ResponseException
import com.grevi.masakapa.util.State
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiHelper: ApiHelper, private val recipesDataSource: RecipesDataSource, private val mapperImpl: MapperImpl) : Repository, ApiResponse() {

    override suspend fun getRecipes() : State<RecipesResponse> {
        return apiResponse { apiHelper.getAllRecipes() }
    }

    override suspend fun getDetailRecipes(key : String) : State<DetailResponse> {
        return apiResponse { apiHelper.getDetailRecipes(key) }
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
                        mapperImpl.categoryMapper(result).let { category ->
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

    override suspend fun insertRecipes(recipesTable : RecipesTable) {
        recipesDataSource.insertRecipes(recipesTable)
    }

    override suspend fun isExistRecipes(key : String) : Boolean {
        return recipesDataSource.isExistRecipes(key)
    }

    override suspend fun getMarkedRecipes() : MutableList<RecipesTable> {
        return recipesDataSource.getMarkRecipes()
    }

    override suspend fun deleteRecipes(recipesTable: RecipesTable) = recipesDataSource.deleteRecipes(recipesTable)

    override suspend fun getFlowRecipes() : Flow<List<RecipesTable>> = recipesDataSource.getFlowRecipes()

    override suspend fun getFlowCategory() : Flow<MutableList<Category>> = recipesDataSource.getFlowCategory()
}