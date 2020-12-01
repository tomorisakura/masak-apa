package com.grevi.masakapa.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.grevi.masakapa.db.entity.Recipes

@Database(entities = [Recipes::class], version = 1, exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDAO() : RecipesDAO
}