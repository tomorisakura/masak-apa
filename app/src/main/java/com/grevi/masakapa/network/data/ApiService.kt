package com.grevi.masakapa.network.data

import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.util.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("/api/recipes")
    suspend fun getAllRecipes() : Response<RecipesResponse>

    @GET("/api/recipe/{key}")
    suspend fun getDetailRecipe(@Path("key") key : String) : Response<DetailResponse>
}