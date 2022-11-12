package com.grevi.masakapa.data.remote

import com.grevi.masakapa.common.state.State
import com.grevi.masakapa.util.ResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber
import java.net.SocketTimeoutException

abstract class ResponseMapper {
    suspend fun <T : Any> launch(
        call: suspend () -> Response<T>,
        saveToLocal: (suspend (T) -> Unit)? = null
    ): Flow<State<T>> {
        State.Loading()
        return flow {
            try {
                val response = call()
                if (response.isSuccessful) {
                    response.body()?.let {
                        saveToLocal?.invoke(it)
                        emit(State.Success(it))
                    } ?: emit(State.Error("Empty Response Body"))
                } else {
                    Timber.e("Response Failure %s", response.errorBody())
                    emit(State.Error("Response failure ${response.errorBody()}"))
                }
            }catch (e: SocketTimeoutException) {
                Timber.e(e.cause.toString(), e.message)
            } catch (e: Exception) {
                Timber.e(e.cause.toString(), e.message)
            }
        }
    }
}