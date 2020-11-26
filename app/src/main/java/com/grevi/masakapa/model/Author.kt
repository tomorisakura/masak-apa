package com.grevi.masakapa.model

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("user") val author : String,
    @SerializedName("datePublished") val published : String
)