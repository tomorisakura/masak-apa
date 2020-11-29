package com.grevi.masakapa.network.response

import com.google.gson.annotations.SerializedName
import com.grevi.masakapa.model.Recipes

data class RecipesResponse(
    @SerializedName("method") var method : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("results") var results : MutableList<Recipes>
)