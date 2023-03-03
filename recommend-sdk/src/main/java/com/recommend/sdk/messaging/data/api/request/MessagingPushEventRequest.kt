package com.recommend.sdk.messaging.data.api.request

/**
 * Messaging push event request
 *
 * @property received
 * @property clicked
 * @property openURL
 * @property eventTime
 * @constructor Create Messaging push event request
 */
data class MessagingPushEventRequest(
    val received: Boolean?,
    val clicked: Boolean?,
    val openURL: String?,
    val eventTime: Int?
)
