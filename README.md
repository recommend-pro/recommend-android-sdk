[<img src="https://raw.githubusercontent.com/recommend-pro/recommend-android-sdk/master/assets/logo.png">](https://recommend.pro)
# Recommend Android SDK
[![GitHub release](https://img.shields.io/github/release/recommend-pro/recommend-android-sdk.svg)](https://github.com/recommend-pro/recommend-android-sdk/releases)
[![License](https://img.shields.io/github/license/recommend-pro/recommend-android-sdk)](https://github.com/recommend-pro/recommend-android-sdk/blob/master/LICENSE)

The Recommend Android SDK allows you to quickly and easily integrate Recommend personal customer experiences in your Android app.

# Table of contents

* [Installation](#installation)
    * [Requirements](#requirements)
    * [Configuration](#configuration)
* [Getting Started](#getting-started)
* [Usage](#usage)
    * [Device activity tracking](#device-activity-tracking)
    * [Push notification](#push-notification)
    * [Recommendation panels](#recommendation-panels)
* [License](#license)

## Installation

### Requirements

* Android 5.0 (API level 21) and above

### Configuration

Add the dependency in your build.gradle:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add `recommend-android-sdk` to your `build.gradle` dependencies:

```
dependencies {
    implementation 'com.recommend:recommend-android-sdk:$latest_version'
}
```

## Getting Started

Before using Recommend SDK, you need to initialize it:

```kotlin
Recommend.init(
    context,
    "recommend_account_id",
    "recommend_application_id",
    "custom_api_host"
)
```

## Usage

### Device activity tracking

You can track device activities using device service:

```kotlin
Recommend.getDeviceService().trackActivity(
  DeviceAddToCartActivity(
    cartHash = "test_cart_hash",
    sku = "item_sku",
    variationSku = "item_variant_sku"
  ),
  Metrics(
    nonInteractive = false,
    data = listOf(
      Metrics.Metric(
        code = "metric_code",
        value = "metric_value"
      )
    )
  )
)
```

List of supported activities can be found [here](https://github.com/recommend-pro/recommend-android-sdk/tree/main/recommend-sdk/src/main/java/com/recommend/sdk/device/data/model/activity).

SDK automatically tracks some activities based on application state. You can disable automatic tracked events:

```kotlin
Recommend.getDeviceService().disableAutoEventTracking()
```

### Push notification

Before using recommend push notification you need to follow Google's guide for setting up and configuring your Firebase app instance. See [Set up a Firebase Cloud Messaging client app on Android](https://firebase.google.com/docs/cloud-messaging/android/client) on the Firebase website. You also need to receive push notifications by reviewing the [Handling messages](https://firebase.google.com/docs/cloud-messaging/android/receive#handling_messages) section as well.

Once Firebase was added to the project, you need to add handling of push notifications and new push tokens.

```kotlin
class FirebaseMessagingService: FirebaseMessagingService() {
  override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
    Recommend.getMessagingService().processMessage(message.data)
  }

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    Recommend.getMessagingService().setPushToken(token)
  }
}
```

You can change subscription status using Message Service.

```kotlin
Recommend.getMessagingService().setSubscriptionStatus(MessagingPushSubscriptionStatus.SUBSCRIBED)
```

**Keep in mind that before changing subscription status you mush set push token to SDK.**

```kotlin
Recommend.getMessagingService().setPushToken(token)
```

For the correct working of smart campaigns that send push notifications, you need to add processing of push clicks. To do this, you need to add Intents handling.

```kotlin
Recommend.getMessagingService().onReceiveIntent(intent)
```

Also you can set push notification icon.

```kotlin
Recommend.getMessagingService().setSmallIconDrawable(iconDrawableId)
```

If you want to completely override the process of creating a notification, or just do something when a Recommend push notification is received, use onReceivePushNotificationListener. If you want to prevent Recommend SDK from creating notifications return `true` in the listener, or return `false` if you want to allow it to SDK.

```kotlin
Recommend.getMessagingService().setOnReceivePushNotificationListener { recommendPush ->
  //Do something with push data
  true
}
```

You can display an Android push notification by following Google's official [guidelines](https://developer.android.com/develop/ui/views/notifications/build-notification).

### Recommendation panels

To get personalised Recommend panels you can use Recommendation Service.

```kotlin
val context = RecommendationPanelRequest.RecommendationPanelRequestContext(
  searchTerm = "search_term",
  currentContext = RecommendationPanelRequest.RecommendationPanelRequestContext.CurrentContext(
    sku = "current_product_sku",
    listId = "current_list_id"
  ),
  skus = listOf("first_product_sku", "second_product_sku"),
  listIds = listOf("first_list_id", "second_list_id")
)

val metrics = Metrics(
  nonInteractive = true,
  data = listOf(
    Metrics.Metric(
      code = "metric_code",
      value = "metric_value"
    )
  )
)

val filters = listOf(
  RecommendationPanelRequest.RecommendationPanelRequestFilter(
    type = RecommendationPanelRequest.RecommendationPanelRequestFilter.Type.PRODUCT,
    code = "filter_code",
    operator = "filter_operator",
    value = "filter_value",
  )
)

val recommendationPanelRequests = RecommendationPanelRequest(
  id = relatedProductCollection.code,
  context = context,
  filters = filters,
  attrsToInclude = listOf(
    "CUSTOM_ATTR_KEY_1",
    "CUSTOM_ATTR_KEY_2",
    "CUSTOM_ATTR_KEY_3"
  ),
  variations = Variations(
    include = true,
    attrsToInclude = listOf("CUSTOM_ATTR_KEY_1", "CUSTOM_ATTR_KEY_2", "CUSTOM_ATTR_KEY_3")
  )
)

Recommend.getRecommendationService().getRecommendPanel(
  onLoad = { panels ->
    //Use panels data to show UI
  },
  onError = { error ->
    //Handle Recommend error
  },
  metrics = metrics,
  pageType = "page_type",
  panelRequests = recommendationPanelRequests
)
```

## License
The contents of this repository are licensed under the
[MIT License](https://github.com/recommend-pro/recommend-android-sdk/blob/main/LICENSE).