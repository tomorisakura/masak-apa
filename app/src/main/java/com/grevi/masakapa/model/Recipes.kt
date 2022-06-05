package com.grevi.masakapa.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipes(
    @SerializedName("title") var name : String = "",
    @SerializedName("thumb") var imageThumb : String = "",
    @SerializedName("key") var key : String = "",
    @SerializedName("times") var times : String = "",
    @SerializedName("portion") var portion : String = "",
    @SerializedName("dificulty") var dificulty : String = ""
) : Parcelable