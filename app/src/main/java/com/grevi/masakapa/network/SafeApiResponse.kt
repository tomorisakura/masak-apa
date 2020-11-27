package com.grevi.masakapa.network

import com.grevi.masakapa.util.ResponseException
import com.grevi.masakapa.util.ResultException
import okio.IOException
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

open class SafeApiResponse {
    suspend fun <T : Any> apiResponse(call : suspend() -> Response<T>) : T {
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
                        is ConnectException -> message.append("Connection Failure")
                        is IOException -> message.append("Failure")
                        else -> message.append("Unknown Exception")
                    }
                }
            }
            throw ResultException(message.toString())
        }
    }
}