package com.grevi.masakapa.data.remote

import com.grevi.masakapa.common.state.State
import com.grevi.masakapa.util.ResponseException
import retrofit2.Response

open class ResponseMapper {
    suspend fun <T : Any> launch(call : suspend() -> Response<T>, saveToLocal: (suspend (T) -> Unit)? = null) : State<T> {
        call.invoke().let {
            return try {
                if (it.isSuccessful) {
                    val body = it.body() ?: throw ResponseException("body is null")
                    saveToLocal?.invoke(body)
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