package com.grevi.masakapa.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkUtils(context: Context) : ConnectivityManager.NetworkCallback() {
    private val _networkDataStatus = MutableLiveData<Boolean>()
    private val appContext = context
    val networkDataStatus : MutableLiveData<Boolean> get() = _networkDataStatus

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() : LiveData<Boolean> {
        val connectivityManager = appContext.getSystemService<ConnectivityManager>()
        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(this)

            val connectionStatus = connectivityManager.allNetworks.any { network ->
                val networkCapability = connectivityManager.getNetworkCapabilities(network)
                return@any networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            }
            _networkDataStatus.postValue(connectionStatus)
        }

        return _networkDataStatus
    }

    override fun onAvailable(network: Network) {
        _networkDataStatus.postValue(true)
    }

    override fun onLost(network: Network) {
        _networkDataStatus.postValue(false)
    }
}