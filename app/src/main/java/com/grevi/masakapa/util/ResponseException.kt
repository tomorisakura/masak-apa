package com.grevi.masakapa.util

import java.io.IOException

open class ResponseException() : IOException() {
    override val message: String?
        get() = "No Internet Connection"
}