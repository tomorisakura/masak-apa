package com.grevi.masakapa.common.coroutine

import com.grevi.masakapa.common.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun BaseFragment<*,*>.coroutineJob(job: Job,invoke: () -> Unit) {
    CoroutineScope(job + Dispatchers.IO).launch {
        invoke()
    }
}