package com.grevi.masakapa.data.remote.response

import com.google.gson.annotations.SerializedName
import com.grevi.masakapa.model.Categorys

data class CategoryResponse(
    @SerializedName("method") var method : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("results") var results : MutableList<Categorys>
)