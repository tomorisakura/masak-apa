package com.grevi.masakapa.repository.mapper

import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.model.Categorys
import com.grevi.masakapa.model.Recipes

interface MapperEntity {
    suspend fun recipesMapper(model : Recipes) : RecipesTable
    suspend fun categoryMapper(model: Categorys) : Category
}