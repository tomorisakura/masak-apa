package com.grevi.masakapa.repository

import com.grevi.masakapa.util.ResponseException
import com.grevi.masakapa.common.state.State

suspend fun <T>Repository.saveToLocalState(state: State<T>, onSave: suspend (T) -> Unit) {
    when(state) {
        is State.Error -> throw ResponseException(state.msg)
        is State.Success -> onSave(state.data)
        else -> Unit
    }
}