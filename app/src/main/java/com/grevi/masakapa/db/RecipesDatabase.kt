package com.grevi.masakapa.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.db.entity.Recipes

@Database(entities = [Recipes::class, Category::class], version = 2, exportSchema = true)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDAO() : RecipesDAO
}