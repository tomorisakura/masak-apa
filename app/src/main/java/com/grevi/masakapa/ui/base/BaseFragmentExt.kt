package com.grevi.masakapa.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.grevi.masakapa.util.State
import com.grevi.masakapa.util.snackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import java.lang.IllegalStateException

fun <T>BaseFragment<*,*>.observeDataListFlow(
    data: MutableStateFlow<State<T>>,
    onSuccessState: (T) -> Unit
) {
    lifecycleScope.launchWhenCreated {
        data.collect {
            when(it) {
                is State.Error -> snackBar(getBinding().root, it.msg).show()
                is State.Loading -> snackBar(getBinding().root, it.msg).show()
                is State.Success -> onSuccessState(it.data)
                else -> throw IllegalStateException("Statenya ngaco bang")
            }
        }
    }
}

fun <T>BaseFragment<*,*>.observeLiveData(data: LiveData<State<T>>, onSuccessState: (T) -> Unit) {
    data.observe(viewLifecycleOwner, {
        when(it) {
            is State.Error -> snackBar(getBinding().root, it.msg).show()
            is State.Loading -> snackBar(getBinding().root, it.msg).show()
            is State.Success -> onSuccessState(it.data)
            else -> throw IllegalStateException("State live datanya ngaco bang")
        }
    })
}