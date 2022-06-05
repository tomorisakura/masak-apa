package com.grevi.masakapa.data.local

import com.grevi.masakapa.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipesDataSourceImpl @Inject constructor(private val recipesDAO: RecipesDAO) :
    RecipesDataSource {
    override suspend fun insertRecipes(recipesTable: RecipesTable) =
        recipesDAO.insertRecipes(recipesTable)

    override suspend fun getFlowRecipes(): Flow<MutableList<RecipesTable>> =
        flow { emit(recipesDAO.getAllMarkRecipes()) }

    override suspend fun insertCategory(category: Category) = recipesDAO.insertCategory(category)
    override suspend fun getAllCategory(): MutableList<Category> = recipesDAO.getAllCategory()
    override suspend fun getFlowCategory(): Flow<MutableList<Category>> =
        flow { emit(recipesDAO.getAllCategory()) }

    override suspend fun insertDetail(detailTable: DetailTable) =
        recipesDAO.insertDetail(detailTable)

    override suspend fun findDetail(name: String): Flow<List<DetailWithIngredientsAndSteps>> =
        flow { emit(recipesDAO.findDetail(name)) }

    override suspend fun insertFavorite(favorite: RecipeFavorite) =
        recipesDAO.insertFavorite(favorite)

    override suspend fun isFavoriteExists(key: String): Boolean = recipesDAO.isFavoriteExists(key)
    override suspend fun getFavorite(): Flow<List<RecipeFavorite>> =
        flow { emit(recipesDAO.getAllFavorite()) }

    override suspend fun deleteFavorite(favorite: RecipeFavorite) =
        recipesDAO.deleteFavorite(favorite)

    override suspend fun findRecipeByDetailName(name: String): Flow<DetailTable> = flow {
        emit(recipesDAO.findRecipeByDetailName(name))
    }
}