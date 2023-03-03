package com.recommend.sdk.core.data.model

/**
 * Current state
 *
 * @property deviceId Current device id. Use for identify device in recommend.
 * @property isFirstLaunch
 * @property isSubscribedToPush
 * @property pushToken
 * @property subscriptionStatusChangeDate
 * @property firstSubscribedDate
 * @constructor Create Current state
 */
data class CurrentState(
    var deviceId: String,
    var isFirstLaunch: Boolean = true,
    var isSubscribedToPush: Boolean? = null,
    var pushToken: String? = null,
    var lastSentManuallySubscribedToPushStatus: Boolean? = null,
    var subscriptionStatusChangeDate: Int? = null,
    var firstSubscribedDate: Int? = null
)
