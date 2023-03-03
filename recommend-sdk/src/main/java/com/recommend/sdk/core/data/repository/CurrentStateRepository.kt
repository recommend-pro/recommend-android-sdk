package com.recommend.sdk.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.recommend.sdk.core.data.model.CurrentState
import kotlinx.coroutines.flow.first
import java.util.*

/**
 * Current state repository
 *
 * @constructor
 *
 * @param context
 */
class CurrentStateRepository(private val context: Context) {
    companion object {
        const val CURRENT_STATE_PREFERENCES_NAME = "RECOMMEND_CURRENT_STATE"
        const val DEVICE_OLD_SDK_PREFERENCES_KEY = "PREF_UNIQUE_ID"
    }

    private val Context.recommendCurrentStateDataStore: DataStore<Preferences> by preferencesDataStore(name = CURRENT_STATE_PREFERENCES_NAME)
    private val dataStore: DataStore<Preferences> = context.recommendCurrentStateDataStore
    private val deviceIdKey = stringPreferencesKey("device_id")
    private val isFirstLaunchKey = booleanPreferencesKey("is_first_launch")
    private val isSubscribedToPushKey = booleanPreferencesKey("is_subscribed_to_push")
    private val pushToken = stringPreferencesKey("push_token")
    private val lastSentIsSubscribedToPushStatusKey = booleanPreferencesKey("last_sent_is_subscribed_to_push_status")
    private val subscriptionStatusChangeDateKey = intPreferencesKey("subscription_status_change_date")
    private val firstSubscribedDateKey = intPreferencesKey("first_subscribed_date")

    suspend fun getCurrentState(): CurrentState {
        val preferences = dataStore.data.first()

        val currentState = if (preferences[deviceIdKey] == null) {
            //Support for old SDK
            val deviceIdFromOldSDK = getDeviceIdFromOldSDK()

            val newCurrentState = CurrentState(
                deviceIdFromOldSDK ?: UUID.randomUUID().toString()
            )
            saveCurrentState(newCurrentState)
            newCurrentState
        } else {
            CurrentState(
                preferences[deviceIdKey]!!,
                preferences[isFirstLaunchKey] ?: true,
                preferences[isSubscribedToPushKey],
                preferences[pushToken],
                preferences[lastSentIsSubscribedToPushStatusKey],
                preferences[subscriptionStatusChangeDateKey],
                preferences[firstSubscribedDateKey]
            )
        }

        return currentState
    }

    suspend fun saveCurrentState(currentState: CurrentState) {
        dataStore.edit { state ->
            state[deviceIdKey] = currentState.deviceId
            state[isFirstLaunchKey] = currentState.isFirstLaunch
            if (currentState.isSubscribedToPush != null) {
                state[isSubscribedToPushKey] = currentState.isSubscribedToPush!!
            }
            if (currentState.pushToken != null) {
                state[pushToken] = currentState.pushToken!!
            }
            if (currentState.lastSentManuallySubscribedToPushStatus != null) {
                state[lastSentIsSubscribedToPushStatusKey] = currentState.lastSentManuallySubscribedToPushStatus!!
            }
            if (currentState.subscriptionStatusChangeDate != null) {
                state[subscriptionStatusChangeDateKey] = currentState.subscriptionStatusChangeDate!!
            }
            if (currentState.firstSubscribedDate != null) {
                state[firstSubscribedDateKey] = currentState.firstSubscribedDate!!
            }
        }
    }

    private fun getDeviceIdFromOldSDK(): String? {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            DEVICE_OLD_SDK_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        )

        return sharedPrefs.getString(
            DEVICE_OLD_SDK_PREFERENCES_KEY,
            null
        )
    }
}
