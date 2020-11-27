package com.grevi.masakapa.model

import com.google.gson.annotations.SerializedName

data class Categorys(
    @SerializedName("category") val category : String,
    @SerializedName("key") val key : String
)