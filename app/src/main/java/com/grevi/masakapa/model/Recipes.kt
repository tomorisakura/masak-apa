package com.grevi.masakapa.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Recipes(
    @Json(name = "title") var name: String = "",
    @Json(name = "thumb") var imageThumb: String = "",
    @Json(name = "key") var key: String = "",
    @Json(name = "times") var times: String = "",
    @Json(name = "serving") var serving: String = "",
    @Json(name = "difficulty") var dificulty: String = ""
) : Parcelable