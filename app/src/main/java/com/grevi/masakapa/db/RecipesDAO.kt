package com.grevi.masakapa.db

import androidx.room.*
import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.RecipesTable

@Dao
interface RecipesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(vararg recipes: RecipesTable)

    @Query("SELECT EXISTS (SELECT 1 FROM recipes WHERE `key` = :key)")
    suspend fun isExistsRecipes(key : String) : Boolean

    @Query("SELECT * FROM recipes")
    suspend fun getAllMarkRecipes() : MutableList<RecipesTable>

    @Delete
    suspend fun deleteRecipes(recipesTable: RecipesTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(vararg category: Category)

    @Query("SELECT * FROM category")
    suspend fun getAllCategory() : MutableList<Category>
}