package com.grevi.masakapa.db

import androidx.room.*
import com.grevi.masakapa.db.entity.Recipes

@Dao
interface RecipesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(vararg recipes: Recipes)

    @Query("SELECT EXISTS (SELECT 1 FROM recipes WHERE `key` = :key)")
    suspend fun isExistsRecipes(key : String) : Boolean

    @Query("SELECT * FROM recipes")
    suspend fun getAllMarkRecipes() : List<Recipes>

    @Delete
    suspend fun deleteRecipes(recipes: Recipes)
}