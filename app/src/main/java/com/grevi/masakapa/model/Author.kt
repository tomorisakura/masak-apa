package com.grevi.masakapa.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Author(
    @Json(name = "author") val author: String = "",
    @Json(name = "datePublished") val published: String = ""
)