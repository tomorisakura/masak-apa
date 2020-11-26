package com.grevi.masakapa.network.data

import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getAllRecipes(): Response<RecipesResponse> {
        return apiService.getAllRecipes()
    }

    override suspend fun getDetailRecipes(key: String): Response<DetailResponse> {
        return apiService.getDetailRecipe(key)
    }
}