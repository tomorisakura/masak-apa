package com.grevi.masakapa.repos

import com.grevi.masakapa.db.RecipesDataSource
import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.Recipes
import com.grevi.masakapa.network.SafeApiResponse
import com.grevi.masakapa.network.data.ApiHelper
import com.grevi.masakapa.network.response.CategorysResponse
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.network.response.SearchResponse
import com.grevi.masakapa.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Remote @Inject constructor(private val apiHelper: ApiHelper, private val recipesDataSource: RecipesDataSource) : SafeApiResponse() {

    suspend fun getRecipes() : Resource<RecipesResponse> {
        return apiResponse { apiHelper.getAllRecipes() }
    }

    suspend fun getDetailRecipes(key : String) : Resource<DetailResponse> {
        return apiResponse { apiHelper.getDetailRecipes(key) }
    }

    suspend fun getSearchRecipe(query : String) : Resource<SearchResponse> {
        return apiResponse { apiHelper.getSearchRecipes(query) }
    }

    suspend fun getCategorys() : Resource<CategorysResponse> {
        return apiResponse { apiHelper.getCategorys() }
    }

    suspend fun getCategoryRecipes(key: String) : Resource<RecipesResponse> {
        return apiResponse { apiHelper.getCategoryRecipes(key) }
    }

    suspend fun insertRecipes(recipes : Recipes) {
        recipesDataSource.insertRecipes(recipes)
    }

    suspend fun isExistRecipes(key : String) : Boolean {
        return recipesDataSource.isExistRecipes(key)
    }

    suspend fun getMarkedRecipes() : MutableList<Recipes> {
        return recipesDataSource.getMarkRecipes()
    }

    suspend fun deleteRecipes(recipes: Recipes) = recipesDataSource.deleteRecipes(recipes)

    suspend fun getFlowRecipes() : Flow<List<Recipes>> = recipesDataSource.getFlowRecipes()

    suspend fun getFlowCategory() : Flow<MutableList<Category>> = recipesDataSource.getFlowCategory()

    suspend fun insertCategory(category: Category) = recipesDataSource.insertCategory(category)
}