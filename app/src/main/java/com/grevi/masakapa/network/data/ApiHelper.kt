package com.grevi.masakapa.network.data

import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun getAllRecipes() : Response<RecipesResponse>
    suspend fun getDetailRecipes(key : String) : Response<DetailResponse>
}