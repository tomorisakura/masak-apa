package com.grevi.masakapa.network

import com.grevi.masakapa.util.ResponseException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

open class SafeApiResponse {
    suspend fun <T : Any> apiResponse(call : suspend () -> Response<T>) : T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(it.toString())
                } catch (e : Exception) {
                    message.append(e.toString())
                    when(e) {
                        is UnknownHostException -> message.append("Unknown host")
                        is ConnectException -> message.append("No Internet")
                        is IOException -> message.append("Failure")
                        else -> message.append("Unknown Exception")
                    }
                }
            }

            message.append("Response Err: ${error.toString()} Status Code : ${response.code()}")
            throw ResponseException(message.toString())
        }
    }
}