package com.grevi.masakapa.util

import android.content.Context
import android.widget.Toast

data class Resource<out T>(val status : Status, val data : T?, val msg : String?) {

    enum class Status {
        SUCCESS,
        LOADING,
        ERROR
    }

    companion object {
        fun <T>success(data: T?, msg: String? = null) : Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

        fun <T>loading(data: T? = null, msg: String?) : Resource<T> {
            return Resource(Status.LOADING, data, msg)
        }

        fun <T>error(data: T?, msg: String?) : Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }
    }

}

fun toast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}