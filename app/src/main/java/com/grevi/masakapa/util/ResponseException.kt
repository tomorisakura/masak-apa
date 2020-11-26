package com.grevi.masakapa.util

import java.io.IOException

open class ResponseException(private val msg : String) : IOException(msg) {
    override val message: String?
        get() = msg
}