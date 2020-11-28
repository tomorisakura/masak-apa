package com.grevi.masakapa.repos

import com.grevi.masakapa.network.data.ApiHelper
import com.grevi.masakapa.network.SafeApiResponse
import com.grevi.masakapa.network.response.CategorysResponse
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.network.response.SearchResponse
import com.grevi.masakapa.util.Resource
import javax.inject.Inject

class Remote @Inject constructor(private val apiHelper: ApiHelper) : SafeApiResponse() {

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
}