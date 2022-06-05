package com.grevi.masakapa.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipesTable(
    @PrimaryKey @ColumnInfo(name = "key") var key : String = "",
    @ColumnInfo(name = "title") var name : String = "",
    @ColumnInfo(name = "thumb") var imageThumb : String = "",
    @ColumnInfo(name = "times") var times : String = "",
    @ColumnInfo(name = "portion") var portion : String = "",
    @ColumnInfo(name = "difficulty") var difficulty : String = ""
)

@Entity(tableName = "favorite")
data class RecipeFavorite(
    @PrimaryKey @ColumnInfo(name = "key") var key : String = "",
    @ColumnInfo(name = "title") var name : String = "",
    @ColumnInfo(name = "thumb") var imageThumb : String = "",
    @ColumnInfo(name = "times") var times : String = "",
    @ColumnInfo(name = "portion") var portion : String = "",
    @ColumnInfo(name = "difficulty") var difficulty : String = ""
)
