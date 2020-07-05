package com.planday.employeesassignment.services.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

class NetworkAvailabilityService {

    companion object {
        private var connectivityManager: ConnectivityManager? = null

        val listeners: HashSet<NetworkAvailabilityObserver> = HashSet()

        val networkCallback : ConnectivityManager.NetworkCallback  = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                listeners.forEach { it.onNetworkAvailabilityChanged(true) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                listeners.forEach { it.onNetworkAvailabilityChanged(false) }
            }
        }

        fun init(context: Context) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            if (connectivityManager is ConnectivityManager) {
                this.connectivityManager = connectivityManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    connectivityManager.registerDefaultNetworkCallback(networkCallback)

                } else {
                    val request: NetworkRequest = NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
                    connectivityManager.registerNetworkCallback(request, networkCallback)
                }
            }

        }

        fun addListener(listener: NetworkAvailabilityObserver) {
            connectivityManager?.isDefaultNetworkActive?.let { listener.onNetworkAvailabilityChanged(it) }
            listeners.add(listener)
        }

        fun removeListener(listener: NetworkAvailabilityObserver) {
            listeners.remove(listener)
        }
    }
}