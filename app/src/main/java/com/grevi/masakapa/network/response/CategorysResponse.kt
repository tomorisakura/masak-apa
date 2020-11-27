package com.grevi.masakapa.network.response

import com.google.gson.annotations.SerializedName
import com.grevi.masakapa.model.Categorys

data class CategorysResponse(
    @SerializedName("method") var method : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("results") var results : MutableList<Categorys>
)