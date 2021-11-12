package com.grevi.masakapa.repository.mapper

import com.grevi.masakapa.data.local.entity.*
import com.grevi.masakapa.model.Categorys
import com.grevi.masakapa.model.Detail
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

    override suspend fun detailMapper(model: Detail): DetailTable {
        return DetailTable(
            name = model.name,
            author = model.author.author,
            published = model.author.published,
            servings = model.servings,
            times = model.times,
            difficulty = model.dificulty
        )
    }

    override suspend fun ingredientMapper(ingredients: String): Ingredients {
        return Ingredients(
            ingredients = ingredients
        )
    }

    override suspend fun stepsMapper(steps: String): Steps {
        return Steps(
            steps = steps
        )
    }
}