package com.grevi.masakapa.data.local.entity

import androidx.room.*
import com.grevi.masakapa.model.Author

@Entity(tableName = "detail")
data class DetailTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "detailId") val id : Int? = null,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "servings") val servings : String,
    @ColumnInfo(name = "times") val times : String,
    @ColumnInfo(name = "difficulty") val difficulty : String,
    @ColumnInfo(name = "author") val author : String,
    @ColumnInfo(name = "published") val published : String,
//    @ColumnInfo(name = "ingredients") val ingredients : List<Ingredients>,
//    @ColumnInfo(name = "steps") val step : List<Steps>
)

data class DetailWithIngredientsAndSteps(
    @Embedded val detail : DetailTable,
    @Relation(
        parentColumn = "detailId",
        entityColumn = "detailIngredientId",
    )
    val ingredients: List<Ingredients>,
    @Relation(
        parentColumn = "detailId",
        entityColumn = "detailStepsId"
    )
    val step: List<Steps>
)

@Entity(tableName = "ingredients")
data class Ingredients(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "detailIngredientId") val detailIngredientId : Int? = null,
    @ColumnInfo(name = "ingredients") val ingredients : String
)

@Entity(tableName = "steps")
data class Steps(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "detailStepsId") val detailStepsId : Int? = null,
    @ColumnInfo(name = "steps") val steps : String
)