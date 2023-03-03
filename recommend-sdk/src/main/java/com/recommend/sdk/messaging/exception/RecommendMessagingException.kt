package com.recommend.sdk.messaging.exception

class RecommendMessagingException(val code: ErrorCode): Throwable(code.errorMessage) {
    enum class ErrorCode(val errorMessage: String) {
        NOTIFICATION_IS_DISABLED("Notification is disabled for this application"),
        PUSH_TOKEN_NOT_SET("Set push token before changing subscription status.")
    }
}
