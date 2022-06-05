package com.grevi.masakapa.data.remote.data

import com.grevi.masakapa.data.remote.response.CategoryResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
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

    override suspend fun getCategory(): Response<CategoryResponse> {
        return apiService.getCategory()
    }

    override suspend fun getCategoryRecipes(key: String): Response<RecipesResponse> {
        return apiService.getCategoryRecipes(key)
    }
}