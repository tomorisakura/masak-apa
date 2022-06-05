package com.grevi.masakapa.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Network(private val context: Context) : ConnectivityManager.NetworkCallback() {
    private val _networkDataStatus = MutableLiveData<Boolean>()
    val networkDataStatus: MutableLiveData<Boolean> = _networkDataStatus

    init {
        observeConnectivity()
    }

    private fun observeConnectivity(): LiveData<Boolean> {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
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