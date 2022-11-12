package com.grevi.masakapa.model

import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("title") val name : String,
    @SerializedName("thumb") val thumbnail : String,
    @SerializedName("servings") val servings : String,
    @SerializedName("times") val times : String,
    @SerializedName("difficulty") val difficulty : String,
    @SerializedName("author") val author : Author,
    @SerializedName("desc") val desc : String,
    @SerializedName("ingredient") val ingredients : MutableList<String>,
    @SerializedName("step") val step : MutableList<String>
)

