package com.grevi.masakapa.common.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.grevi.masakapa.common.popup.snackBar
import com.grevi.masakapa.common.state.State
import com.grevi.masakapa.util.Constant.ZERO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

fun <T> BaseFragment<*, *>.observeDataFlow(
    data: MutableStateFlow<State<T>>,
    onSuccessState: (T) -> Unit
) {
    lifecycleScope.launchWhenCreated {
        data.collect {
            when (it) {
                is State.Error -> snackBar(getBinding().root, it.msg).show()
                is State.Loading -> snackBar(getBinding().root, it.msg).show()
                is State.Success -> onSuccessState(it.data)
                else -> Unit
            }
        }
    }
}

fun <T> BaseFragment<*, *>.observeLiveData(
    data: LiveData<State<T>>,
    onLoading: (() -> Unit)? = null,
    onSuccessState: (T) -> Unit
) {
    data.observe(viewLifecycleOwner) {
        when (it) {
            is State.Error -> snackBar(getBinding().root, it.msg).show()
            is State.Loading -> {
                onLoading?.invoke()
                snackBar(getBinding().root, it.msg).show()
            }
            is State.Success -> onSuccessState(it.data)
            else -> Unit
        }
    }
}

fun BaseFragment<*, *>.showSoftKey(view: View, state: Boolean) {
    when (state) {
        true -> {
            view.requestFocus().run {
                val input =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                input.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        false -> {
            view.requestFocus().run {
                val input =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                input.hideSoftInputFromWindow(view.windowToken, ZERO)
            }
        }
    }
}