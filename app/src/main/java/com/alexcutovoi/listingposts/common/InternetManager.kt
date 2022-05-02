package com.alexcutovoi.listingposts.common

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

class InternetManager(private val connectivityManager: ConnectivityManager?) {
    fun checkInternet(): Boolean {
        connectivityManager?.let { manager ->
            val currentNetwork: Network = manager.activeNetwork ?: return false
            val currentNetworkCapabilities: NetworkCapabilities =
                manager.getNetworkCapabilities(currentNetwork) ?: return false

            val networkTransports = arrayListOf(
                NetworkCapabilities.TRANSPORT_CELLULAR,
                NetworkCapabilities.TRANSPORT_WIFI,
                NetworkCapabilities.TRANSPORT_VPN,
                NetworkCapabilities.TRANSPORT_ETHERNET
            )


            networkTransports.forEach { transport ->
                if (currentNetworkCapabilities.hasTransport(transport)) return true
            }
        }
        return false
    }

    fun onInternet(checkCallback: () -> Unit) {
        if(checkInternet()) checkCallback()
    }
}