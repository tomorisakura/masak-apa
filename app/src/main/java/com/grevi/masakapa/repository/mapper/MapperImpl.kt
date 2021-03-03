package com.grevi.masakapa.repository.mapper

import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.model.Categorys
import com.grevi.masakapa.model.Recipes
import javax.inject.Inject

class MapperImpl @Inject constructor() : MapperEntity {

    override suspend fun recipesMapper(model: Recipes): RecipesTable = RecipesTable(
        key = model.key,
        name = model.name,
        imageThumb = model.imageThumb,
        times = model.times,
        portion = model.portion,
        dificulty = model.dificulty
    )

    override suspend fun categoryMapper(model: Categorys): Category = Category(
        category = model.category,
        key = model.key
    )
}