package com.grevi.masakapa.network.data

import com.grevi.masakapa.network.response.CategorysResponse
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.network.response.SearchResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getAllRecipes(): Response<RecipesResponse> {
        return apiService.getAllRecipes()
    }

    override suspend fun getDetailRecipes(key: String): Response<DetailResponse> {
        return apiService.getDetailRecipe(key)
    }

    override suspend fun getSearchRecipes(query: String): Response<SearchResponse> {
        return apiService.getSearchRecipe(query)
    }

    override suspend fun getCategorys(): Response<CategorysResponse> {
        return apiService.getCategorys()
    }
}