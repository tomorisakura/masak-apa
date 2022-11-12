package com.grevi.masakapa.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    @OptIn(InternalCoroutinesApi::class)
    fun <T> collectFlow(stateFlow: Flow<T>, liveData: MutableLiveData<T>) {
        val collector = object : FlowCollector<T> {
            override suspend fun emit(value: T) {
                liveData.postValue(value)
            }
        }
        viewModelScope.launch { stateFlow.collect(collector) }
    }
}