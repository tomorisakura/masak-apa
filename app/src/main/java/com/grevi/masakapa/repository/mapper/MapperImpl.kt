package com.grevi.masakapa.repository.mapper

import com.grevi.masakapa.data.local.RecipesDataSource
import com.grevi.masakapa.data.local.entity.*
import com.grevi.masakapa.data.remote.response.CategoryResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import javax.inject.Inject

class MapperImpl @Inject constructor(
    private val recipesDataSource: RecipesDataSource
) : MapperEntity {

    override suspend fun recipesMapper(response: RecipesResponse) {
        response.results.map { model ->
            recipesDataSource.insertRecipes(
                RecipesTable(
                    key = model.key,
                    name = model.name,
                    imageThumb = model.imageThumb,
                    times = model.times,
                    portion = model.serving,
                    difficulty = model.dificulty
                )
            )
        }
    }

    override suspend fun categoryMapper(response: CategoryResponse) {
        response.results.map { model ->
            recipesDataSource.insertCategory(
                Category(
                    category = model.category,
                    key = model.key
                )
            )
        }
    }

    override suspend fun detailMapper(response: DetailResponse) {
        recipesDataSource.insertDetail(
            DetailTable(
                name = response.results.name,
                author = response.results.author.author,
                published = response.results.author.published,
                servings = response.results.servings,
                times = response.results.times,
                difficulty = response.results.difficulty
            )
        )
    }
}