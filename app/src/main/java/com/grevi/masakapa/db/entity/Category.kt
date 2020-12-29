package com.grevi.masakapa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey @ColumnInfo(name = "key") var key : String,
    @ColumnInfo(name = "category") var category : String
)