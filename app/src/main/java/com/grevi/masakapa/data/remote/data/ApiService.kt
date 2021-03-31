package com.grevi.masakapa.data.remote.data

import com.grevi.masakapa.data.remote.response.CategorysResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/api/recipes-length/?limit=5")
    suspend fun getAllRecipes() : Response<RecipesResponse>

    @GET("/api/recipe/{key}")
    suspend fun getDetailRecipe(@Path("key") key : String) : Response<DetailResponse>

    @GET("/api/search/")
    suspend fun getSearchRecipe(@Query("q") query : String) : Response<SearchResponse>

    @GET("/api/categorys/recipes")
    suspend fun getCategorys() : Response<CategorysResponse>

    @GET("/api/categorys/recipes/{key}")
    suspend fun getCategoryRecipes(@Path("key") key: String) : Response<RecipesResponse>
}