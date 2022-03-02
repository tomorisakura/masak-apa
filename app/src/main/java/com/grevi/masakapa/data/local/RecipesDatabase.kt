package com.grevi.masakapa.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.grevi.masakapa.data.local.entity.*

@Database(entities = [
    RecipesTable::class,
    Category::class,
    DetailTable::class,
    Ingredients::class,
    Steps::class,
    RecipeFavorite::class], version = 5, exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDAO() : RecipesDAO
}