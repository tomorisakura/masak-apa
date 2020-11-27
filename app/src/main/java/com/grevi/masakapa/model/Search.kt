package com.grevi.masakapa.model

import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("title") var name : String,
    @SerializedName("thumb") var imageThumb : String,
    @SerializedName("key") var key : String,
    @SerializedName("times") var times : String,
    @SerializedName("serving") var servings : String,
    @SerializedName("difficulty") var difficulty : String
)