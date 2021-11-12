package com.grevi.masakapa.repository.mapper

import com.grevi.masakapa.data.local.entity.*
import com.grevi.masakapa.model.Categorys
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.model.Recipes

interface MapperEntity {
    suspend fun recipesMapper(model : Recipes) : RecipesTable
    suspend fun categoryMapper(model: Categorys) : Category
    suspend fun detailMapper(model : Detail) : DetailTable
    suspend fun ingredientMapper(ingredients: String) : Ingredients
    suspend fun stepsMapper(steps : String) : Steps
}