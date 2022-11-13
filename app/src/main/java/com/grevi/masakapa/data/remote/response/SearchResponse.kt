package com.grevi.masakapa.data.remote.response

import com.grevi.masakapa.model.Search
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "method") val method: String,
    @Json(name = "status") val status: Boolean,
    @Json(name = "results") val results: MutableList<Search>
)