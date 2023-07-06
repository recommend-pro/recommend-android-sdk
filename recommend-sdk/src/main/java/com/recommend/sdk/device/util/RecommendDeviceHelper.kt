package com.recommend.sdk.device.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.recommend.sdk.core.data.model.Metrics

class RecommendDeviceHelper {
    companion object {
        fun getCurrentApplicationVersionName(context: Context): String {
            return try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                ""
            }
        }

        fun getDeviceName(context: Context): String {
            return try {
                var deviceName = Settings.System.getString(context.contentResolver, "bluetooth_name")

                if (deviceName.isNullOrEmpty()) {
                    deviceName = Settings.Secure.getString(context.contentResolver, "bluetooth_name")
                }

                deviceName ?: (Build.MODEL + " " + Build.MANUFACTURER)
            } catch (e: Throwable) {
                Build.MODEL + " " + Build.MANUFACTURER
            }
        }

        fun mergeMetrics(defaultMetrics: Metrics, newMetrics: Metrics): Metrics {
            val metricsData = mutableListOf<Metrics.Metric>()

            val metricsMap = mutableMapOf<String, String>()
            defaultMetrics.data.forEach {
                metricsMap[it.code] = it.value
            }

            newMetrics.data.forEach {
                metricsMap[it.code] = it.value
            }

            metricsMap.forEach{
                metricsData.add(Metrics.Metric(it.key, it.value))
            }

            return Metrics(
                metricsData,
                newMetrics.nonInteractive
            )
        }

        fun getDetectedCountry(context: Context, defaultCountryIsoCode: String): String {
            detectSIMCountry(context)?.let {
                return it
            }

            detectNetworkCountry(context)?.let {
                return it
            }

            detectLocaleCountry(context)?.let {
                return it
            }

            return defaultCountryIsoCode
        }

        private fun detectSIMCountry(context: Context): String? {
            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager.simCountryIso
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        private fun detectNetworkCountry(context: Context): String? {
            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager.networkCountryIso
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        private fun detectLocaleCountry(context: Context): String? {
            try {
                val localeCountryISO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.resources.configuration.locales[0].country
                } else {
                    context.resources.configuration.locale.country
                }
                return localeCountryISO
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}
