package com.grevi.masakapa.repository.mapper

import com.grevi.masakapa.data.local.entity.*
import com.grevi.masakapa.data.remote.response.CategorysResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.model.Categorys
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.model.Recipes

interface MapperEntity {
    suspend fun recipesMapper(response : RecipesResponse)
    suspend fun categoryMapper(response: CategorysResponse)
    suspend fun detailMapper(response : DetailResponse)
}