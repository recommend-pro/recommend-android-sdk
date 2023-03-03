package com.recommend.recommendexampleapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recommend.recommendexampleapplication.ui.theme.RecommendExampleApplicationTheme
import com.recommend.sdk.Recommend
import com.recommend.sdk.core.data.model.Metrics
import com.recommend.sdk.device.data.model.activity.DeviceAddToCartActivity
import com.recommend.sdk.messaging.data.model.MessagingPushSubscriptionStatus

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Recommend.getMessagingService().setOnReceivePushNotificationListener { recommendPush ->
            //Do something with push data
            true
        }

        setContent {
            RecommendExampleApplicationTheme {
                TestRecommendView(
                    {
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
                    },
                    {
                        Recommend.getDeviceService().disableAutoEventTracking()
                    },
                    { subscribe ->
                        Recommend.getMessagingService().setSubscriptionStatus(if (subscribe) MessagingPushSubscriptionStatus.SUBSCRIBED else MessagingPushSubscriptionStatus.UNSUBSCRIBED)
                    }
                )
            }
        }
    }
}

@Composable
fun TestRecommendView(
    onClickTrackCartEvent: () -> Unit,
    disableAutomaticTrackedEvents: () -> Unit,
    onChangeSubscriptionStatus: (subscribe: Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp)
    ) {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Recommend Test Application")
        }
        Surface(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { onClickTrackCartEvent() }) {
                Text(text = "Track cart event")
            }
        }
        Surface(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { disableAutomaticTrackedEvents() }) {
                Text(text = "Disable automatic tracked events")
            }
        }
        Surface(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    onClick = { onChangeSubscriptionStatus(true) }
                ) {
                    Text(text = "Subscribe", textAlign = TextAlign.Center)
                }
                Spacer(Modifier.width(20.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    onClick = { onChangeSubscriptionStatus(false) }
                ) {
                    Text(text = "Unsubscribe", textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecommendExampleApplicationTheme {
        TestRecommendView({}, {}, {})
    }
}