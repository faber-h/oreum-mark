@file:Suppress("DEPRECATION")

package com.okre.oreummark.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class OreumNetworkManager(val context: Context) {

    fun checkNetworkState() : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // Deprecated in Java -> @file:Suppress("DEPRECATION")
            val activeNetworkInfo = connectivityManager.activeNetworkInfo ?: return false
            return activeNetworkInfo.isConnected
        }
    }
}