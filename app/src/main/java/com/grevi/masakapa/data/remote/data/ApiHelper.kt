package com.grevi.masakapa.data.remote.data

import com.grevi.masakapa.data.remote.response.CategorysResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun getAllRecipes() : Response<RecipesResponse>
    suspend fun getDetailRecipes(key : String) : Response<DetailResponse>
    suspend fun getSearchRecipes(query : String) : Response<SearchResponse>
    suspend fun getCategorys() : Response<CategorysResponse>
    suspend fun getCategoryRecipes(key: String) : Response<RecipesResponse>
}