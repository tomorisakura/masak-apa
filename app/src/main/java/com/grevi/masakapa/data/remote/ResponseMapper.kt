package com.grevi.masakapa.data.remote

import com.grevi.masakapa.common.state.State
import com.grevi.masakapa.util.ResponseException
import retrofit2.Response
import timber.log.Timber
import java.net.SocketTimeoutException

abstract class ResponseMapper {
    suspend fun <T : Any> launch(
        call: suspend () -> Response<T>,
        saveToLocal: (suspend (T) -> Unit)? = null
    ): State<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let {
                    saveToLocal?.invoke(it)
                    State.Success(it)
                } ?: State.Error("Empty Response Body")
            } else {
                State.Error("Error Body : ${response.errorBody()}")
            }
        } catch (e: SocketTimeoutException) {
            Timber.e(e)
            State.Error("Socket Timeout ${e.message}")
        } catch (ex: Exception) {
            Timber.e(ex)
            State.Error("Exception ${ex.message}")
        }
    }
}