package com.grevi.masakapa.network

import com.grevi.masakapa.util.ResponseException
import com.grevi.masakapa.util.State
import retrofit2.Response

open class ApiResponse {
    suspend fun <T : Any> apiResponse(call : suspend() -> Response<T>) : State<T> {
        call.invoke().let {
            try {
                if (it.isSuccessful) {
                    val body = it.body() ?: return@let
                     return State.Success(body)
                } else {
                    return State.Error("Error Body : ${it.errorBody()}")
                }
            }catch (e : Exception) {
                throw ResponseException(e.toString())
            }
        }
        return State.Data
    }
}