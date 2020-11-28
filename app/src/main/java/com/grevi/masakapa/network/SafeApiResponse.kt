package com.grevi.masakapa.network

import com.grevi.masakapa.util.Resource
import okhttp3.OkHttpClient
import okio.IOException
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

open class SafeApiResponse {
    suspend fun <T : Any> apiResponse(call : suspend() -> Response<T>) : Resource<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            Resource.error(null, "${response.code()} : ${response.errorBody()}")
        }catch (e : Exception) {
            return when(e) {
                is UnknownHostException -> Resource.error(null, "url is invalid")
                is ConnectException -> Resource.error(null, "connection failure")
                is IOException -> Resource.error(null,"no inet")
                is OkHttpClient -> Resource.error(null, "unable host")
                else -> error(e.message ?: e.toString())
            }
        }
    }
}