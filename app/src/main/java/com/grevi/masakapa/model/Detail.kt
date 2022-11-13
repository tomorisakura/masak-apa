package com.grevi.masakapa.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Detail(
    @Json(name = "title") val name: String = "",
    @Json(name = "thumb") val thumbnail: String? = "",
    @Json(name = "servings") val servings: String = "",
    @Json(name = "times") val times: String = "",
    @Json(name = "difficulty") val difficulty: String = "",
    @Json(name = "author") val author: Author = Author(),
    @Json(name = "desc") val desc: String = "",
    @Json(name = "ingredient") val ingredients: List<String> = listOf(),
    @Json(name = "step") val step: List<String> = listOf()
)

