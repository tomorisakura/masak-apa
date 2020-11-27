package com.grevi.masakapa.repos

import com.grevi.masakapa.network.data.ApiHelper
import com.grevi.masakapa.network.SafeApiResponse
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.util.Resource
import javax.inject.Inject

class Remote @Inject constructor(private val apiHelper: ApiHelper) : SafeApiResponse() {

    suspend fun getRecipes() : RecipesResponse {
        return apiResponse { apiHelper.getAllRecipes() }
    }

    suspend fun getDetailRecipes(key : String) : DetailResponse {
        return apiResponse { apiHelper.getDetailRecipes(key) }
    }
}