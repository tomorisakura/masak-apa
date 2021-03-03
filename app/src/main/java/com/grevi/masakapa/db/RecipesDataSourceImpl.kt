package com.grevi.masakapa.db

import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.RecipesTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipesDataSourceImpl @Inject constructor(private val recipesDAO: RecipesDAO) : RecipesDataSource {
    override suspend fun insertRecipes(recipesTable: RecipesTable) = recipesDAO.insertRecipes(recipesTable)
    override suspend fun isExistRecipes(key: String) : Boolean = recipesDAO.isExistsRecipes(key)
    override suspend fun getMarkRecipes() : MutableList<RecipesTable> = recipesDAO.getAllMarkRecipes()
    override suspend fun deleteRecipes(recipesTable: RecipesTable) = recipesDAO.deleteRecipes(recipesTable)
    override suspend fun getFlowRecipes(): Flow<List<RecipesTable>> {
        return flow {
            emit(recipesDAO.getAllMarkRecipes())
        }
    }

    override suspend fun insertCategory(category: Category) = recipesDAO.insertCategory(category)

    override suspend fun getAllCategory(): MutableList<Category> {
        return recipesDAO.getAllCategory()
    }

    override suspend fun getFlowCategory(): Flow<MutableList<Category>> {
        return flow {
            emit(recipesDAO.getAllCategory())
        }
    }

}