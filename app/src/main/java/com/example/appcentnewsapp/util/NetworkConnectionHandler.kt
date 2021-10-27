package com.example.appcentnewsapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.internal.RegisterListenerMethod

object NetworkConnectionHandler : ConnectivityManager.NetworkCallback(){
    var connection : MutableLiveData<Boolean> = MutableLiveData()

    fun getNetworkState(context: Context) : LiveData<Boolean> {
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            connectivityManager.registerDefaultNetworkCallback(this)

        }else{
            val builder=NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(),this)

        }

        var isConnected=false

        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        connection.postValue(isConnected)

        return connection
    }

    override fun onAvailable(network: Network) {
        connection.postValue(true)
    }

    override fun onLost(network: Network) {
        connection.postValue(false)
    }



}