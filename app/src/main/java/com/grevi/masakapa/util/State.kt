package com.grevi.masakapa.util

sealed class State<out T> {
    object Data : State<Nothing>()
    data class Success<T>(val data : T) : State<T>()
    data class Loading(val msg : String = "Loading") : State<Nothing>()
    data class Error(val msg : String) : State<Nothing>()
}
