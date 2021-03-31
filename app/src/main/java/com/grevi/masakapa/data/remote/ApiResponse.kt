package com.grevi.masakapa.data.remote

import com.grevi.masakapa.util.ResponseException
import com.grevi.masakapa.util.State
import retrofit2.Response

open class ApiResponse {
    suspend fun <T : Any> apiResponse(call : suspend() -> Response<T>) : State<T> {
        call.invoke().let {
            return try {
                if (it.isSuccessful) {
                    val body = it.body() ?: throw ResponseException("body is null")
                    State.Success(body)
                } else {
                    State.Error("Error Body : ${it.errorBody()}")
                }
            }catch (e : Exception) {
                throw ResponseException(e.toString())
            }
        }
    }
}