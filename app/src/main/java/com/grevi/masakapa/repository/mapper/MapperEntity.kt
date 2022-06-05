package com.grevi.masakapa.repository.mapper

import com.grevi.masakapa.data.remote.response.CategoryResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse

interface MapperEntity {
    suspend fun recipesMapper(response : RecipesResponse)
    suspend fun categoryMapper(response: CategoryResponse)
    suspend fun detailMapper(response : DetailResponse)
}