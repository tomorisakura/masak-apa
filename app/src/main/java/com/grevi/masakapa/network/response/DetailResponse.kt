package com.grevi.masakapa.network.response

import com.google.gson.annotations.SerializedName
import com.grevi.masakapa.model.Detail

data class DetailResponse(
    @SerializedName("method") val method : String,
    @SerializedName("status") val status : Boolean,
    @SerializedName("results") val results : Detail

)