package com.grevi.masakapa.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Search(
    @Json(name = "title") var name: String,
    @Json(name = "thumb") var imageThumb: String,
    @Json(name = "key") var key: String,
    @Json(name = "times") var times: String,
    @Json(name = "serving") var servings: String,
    @Json(name = "difficulty") var difficulty: String
)