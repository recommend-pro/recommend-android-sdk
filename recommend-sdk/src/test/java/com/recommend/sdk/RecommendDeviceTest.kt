package com.recommend.sdk

import com.recommend.sdk.core.data.model.Metrics
import com.recommend.sdk.core.util.equalsIgnoreOrder
import com.recommend.sdk.device.util.RecommendDeviceHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class RecommendDeviceTest {
    private val defaultMetrics1 = Metrics(
        false,
        listOf(
            Metrics.Metric(
                "default_metric_1",
                "default_value_1"
            ),
            Metrics.Metric(
                "default_metric_2",
                "default_value_2"
            )
        )
    )
    private val defaultMetrics2 = Metrics(
        true,
        listOf(
            Metrics.Metric(
                "default_metric_1",
                "default_value_1"
            ),
            Metrics.Metric(
                "default_metric_2",
                "default_value_2"
            )
        )
    )

    @Test
    fun `merging default and new metrics 1`() {
        val newMetrics = Metrics(
            true,
            listOf(
                Metrics.Metric(
                    "default_metric_1",
                    "new_value_1"
                ),
                Metrics.Metric(
                    "new_metric_2",
                    "new_value_2"
                )
            )
        )

        val result = RecommendDeviceHelper.mergeMetrics(
            defaultMetrics1,
            newMetrics
        )

        val expectedMetricList = listOf(
            Metrics.Metric(
                "default_metric_1",
                "new_value_1"
            ),
            Metrics.Metric(
                "default_metric_2",
                "default_value_2"
            ),
            Metrics.Metric(
                "new_metric_2",
                "new_value_2"
            )
        )

        Assert.assertEquals(
            true,
            result.nonInteractive == true
                    && result.data.equalsIgnoreOrder(expectedMetricList)
        )
    }

    @Test
    fun `merging default and new metrics 2`() {
        val newMetrics = Metrics(
            true,
            listOf(
                Metrics.Metric(
                    "default_metric_1",
                    "new_value_1"
                ),
                Metrics.Metric(
                    "default_metric_2",
                    "new_value_2"
                )
            )
        )

        val result = RecommendDeviceHelper.mergeMetrics(
            defaultMetrics1,
            newMetrics
        )

        val expectedMetricList = listOf(
            Metrics.Metric(
                "default_metric_1",
                "new_value_1"
            ),
            Metrics.Metric(
                "default_metric_2",
                "new_value_2"
            )
        )

        Assert.assertEquals(
            true,
            result.nonInteractive == true
                    && result.data.equalsIgnoreOrder(expectedMetricList)
        )
    }

    @Test
    fun `merging default and new metrics 3`() {
        val newMetrics = Metrics(
            false,
            listOf(
                Metrics.Metric(
                    "default_metric_1",
                    "new_value_1"
                ),
                Metrics.Metric(
                    "default_metric_2",
                    "new_value_2"
                ),
                Metrics.Metric(
                    "new_metric_3",
                    "new_value_3"
                )
            )
        )

        val result = RecommendDeviceHelper.mergeMetrics(
            defaultMetrics2,
            newMetrics
        )

        val expectedMetricList = listOf(
            Metrics.Metric(
                "default_metric_1",
                "new_value_1"
            ),
            Metrics.Metric(
                "default_metric_2",
                "new_value_2"
            ),
            Metrics.Metric(
                "new_metric_3",
                "new_value_3"
            )
        )

        Assert.assertEquals(
            true,
            result.nonInteractive == false
                    && result.data.equalsIgnoreOrder(expectedMetricList)
        )
    }
}
