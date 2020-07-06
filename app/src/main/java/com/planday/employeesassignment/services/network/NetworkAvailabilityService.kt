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
            listener.onNetworkAvailabilityChanged(isNetworkAvailable())
            listeners.add(listener)
        }

        fun removeListener(listener: NetworkAvailabilityObserver) {
            listeners.remove(listener)
        }

        fun isNetworkAvailable() : Boolean {
            val connectivityManager = connectivityManager ?: return false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw      = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else {
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                return nwInfo.isConnected
            }
        }
    }
}