package com.grevi.masakapa.common.coroutine

import com.grevi.masakapa.common.base.BaseFragment
import kotlinx.coroutines.*

fun BaseFragment<*,*>.runTask(invoke: suspend () -> Unit) = runBlocking {
    CoroutineScope(job + Dispatchers.IO).launch { invoke() }
}