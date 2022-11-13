package com.grevi.masakapa.data.remote.response

import com.grevi.masakapa.model.Detail
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailResponse(
    @Json(name = "method") val method: String,
    @Json(name = "status") val status: Boolean,
    @Json(name = "results") val results: Detail
)