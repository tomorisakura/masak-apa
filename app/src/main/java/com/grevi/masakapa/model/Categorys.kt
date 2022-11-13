package com.grevi.masakapa.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Categorys(
    @Json(name = "category") val category: String,
    @Json(name = "key") val key: String
)