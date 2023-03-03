package com.recommend.sdk.core.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.recommend.sdk.core.data.api.UniqueIdTag
import okhttp3.Request
import retrofit2.Invocation

class ApiHelper(private val context: Context) {
    fun isInternetAvailable(): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            }
        }

        return result
    }

    companion object {
        fun getUniqueIdTagFromRequest(request: Request): UniqueIdTag? {
            val invocation = request.tag(Invocation::class.java) ?: return null

            invocation.arguments().forEach {
                if (it is UniqueIdTag) {
                    return it
                }
            }

            return null
        }
    }
}
