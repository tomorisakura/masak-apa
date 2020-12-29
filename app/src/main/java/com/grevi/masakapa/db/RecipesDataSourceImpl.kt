package com.grevi.masakapa.db

import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.Recipes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipesDataSourceImpl @Inject constructor(private val recipesDAO: RecipesDAO) : RecipesDataSource {
    override suspend fun insertRecipes(recipes: Recipes) = recipesDAO.insertRecipes(recipes)
    override suspend fun isExistRecipes(key: String) : Boolean = recipesDAO.isExistsRecipes(key)
    override suspend fun getMarkRecipes() : MutableList<Recipes> = recipesDAO.getAllMarkRecipes()
    override suspend fun deleteRecipes(recipes: Recipes) = recipesDAO.deleteRecipes(recipes)
    override suspend fun getFlowRecipes(): Flow<List<Recipes>> {
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