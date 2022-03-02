package com.grevi.masakapa.data.local

import androidx.room.*
import com.grevi.masakapa.data.local.entity.*

@Dao
interface RecipesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(vararg recipes: RecipesTable)

    @Query("SELECT * FROM recipes")
    suspend fun getAllMarkRecipes() : MutableList<RecipesTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(vararg category: Category)

    @Query("SELECT * FROM category")
    suspend fun getAllCategory() : MutableList<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetail(detailTable : DetailTable)

    @Transaction
    @Query("SELECT * FROM detail WHERE `name` =:name")
    suspend fun findDetail(name : String) : List<DetailWithIngredientsAndSteps>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(vararg recipesFavorite: RecipeFavorite)

    @Query("SELECT EXISTS (SELECT 1 FROM favorite WHERE `key` = :key)")
    suspend fun isFavoriteExists(key : String) : Boolean

    @Query("SELECT * FROM favorite")
    suspend fun getAllFavorite() : MutableList<RecipeFavorite>

    @Query("SELECT * FROM detail WHERE name = :name")
    suspend fun findRecipeByDetailName(name: String): DetailTable

    @Delete
    suspend fun deleteFavorite(recipeFavorite: RecipeFavorite)

}