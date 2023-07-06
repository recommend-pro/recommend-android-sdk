package com.recommend.sdk

import com.recommend.sdk.core.data.model.Metrics
import com.recommend.sdk.core.data.model.PriceList
import com.recommend.sdk.core.util.JsonHelper
import com.recommend.sdk.recommendation.data.api.request.RecommendationPanelRequest
import com.recommend.sdk.recommendation.data.api.request.RecommendationPanelsRequest
import com.recommend.sdk.recommendation.data.model.PanelContentType
import org.junit.Test
import junit.framework.TestCase.assertEquals

class JsonSerializeTest {
    @Test
    fun `test nullable serialization with nullable values in nested objects`() {
        val currentContext = RecommendationPanelRequest.RecommendationPanelRequestContext.CurrentContext(
            sku = "test_sku",
            listId = ""
        )
        val context = RecommendationPanelRequest.RecommendationPanelRequestContext(
            searchTerm = "",
            currentContext = currentContext,
            skus = emptyList(),
            listIds = emptyList()
        )

        val metrics = Metrics(
            nonInteractive = true,
            data = listOf(
                Metrics.Metric(
                    code = "delivery_country",
                    value = "GB"
                )
            )
        )

        val recommendationPanelRequests = listOf(
            RecommendationPanelRequest(
                id = "test_panel_code",
                context = context,
                filters = listOf(
                    RecommendationPanelRequest.RecommendationPanelRequestFilter(
                        code = "filter_code",
                        operator = "eq",
                        value = "value",
                    ),
                    RecommendationPanelRequest.RecommendationPanelRequestFilter(
                        code = "filter_code",
                        operator = "eq",
                        value = "value",
                        type = "product"
                    )
                ),
                attrsToInclude = listOf(
                    "designer",
                    "is_sale",
                    "small_image"
                ),
                variations = null
            )
        )

        val recommendationPanelsRequest = RecommendationPanelsRequest(
            "8ce554fe-a8b0-4d69-9196-06028c0ada4e",
            "28405844-d95d-40b7-80f4-e2e53908c9ae_customer",
            "email_hash_value",
            "GB",
            "GBP",
            "default",
            PriceList("test", PriceList.FallbackMode.DEFAULT, listOf("default")),
            metrics,
            "Product Details",
            PanelContentType.JSON,
            recommendationPanelRequests,
            null
        )

        assertEquals(
            JsonHelper.toJson(recommendationPanelsRequest),
            "{\"device_id\":\"8ce554fe-a8b0-4d69-9196-06028c0ada4e\",\"customer_id_hash\":\"28405844-d95d-40b7-80f4-e2e53908c9ae_customer\",\"email_hash\":\"email_hash_value\",\"store_code\":\"GB\",\"currency_code\":\"GBP\",\"environment_code\":\"default\",\"price_list\":{\"code\":\"test\",\"fallback_mode\":\"default\",\"alternatives\":[\"default\"]},\"metrics\":{\"data\":[{\"code\":\"delivery_country\",\"value\":\"GB\"}],\"non_interactive\":true},\"page_type\":\"Product Details\",\"content_type\":\"json\",\"panels\":[{\"id\":\"test_panel_code\",\"context\":{\"current\":{\"sku\":\"test_sku\",\"list_id\":\"\"},\"search_term\":\"\",\"skus\":[],\"list_ids\":[]},\"filter\":[{\"code\":\"filter_code\",\"operator\":\"eq\",\"value\":\"value\"},{\"code\":\"filter_code\",\"operator\":\"eq\",\"value\":\"value\",\"type\":\"product\"}],\"attrs_to_include\":[\"designer\",\"is_sale\",\"small_image\"]}]}"
        )
    }

    @Test
    fun `test nullable serialization with nullable values in root object and with nullable values in nested objects`() {
        val currentContext = RecommendationPanelRequest.RecommendationPanelRequestContext.CurrentContext(
            sku = "test_sku",
            listId = ""
        )
        val context = RecommendationPanelRequest.RecommendationPanelRequestContext(
            searchTerm = "",
            currentContext = currentContext,
            skus = emptyList(),
            listIds = emptyList()
        )

        val metrics = Metrics(
            nonInteractive = true,
            data = listOf(
                Metrics.Metric(
                    code = "delivery_country",
                    value = "GB"
                )
            )
        )

        val recommendationPanelRequests = listOf(
            RecommendationPanelRequest(
                id = "test_panel_code",
                context = context,
                filters = listOf(
                    RecommendationPanelRequest.RecommendationPanelRequestFilter(
                        type = "product",
                        code = "filter_code",
                        operator = "eq",
                        value = "value",
                    )
                ),
                attrsToInclude = listOf(
                    "designer",
                    "is_sale",
                    "small_image"
                ),
                variations = null
            )
        )

        val recommendationPanelsRequest = RecommendationPanelsRequest(
            "8ce554fe-a8b0-4d69-9196-06028c0ada4e",
            null,
            "email_hash_value",
            "GB",
            "GBP",
            null,
            PriceList("test", PriceList.FallbackMode.DEFAULT, listOf("default")),
            metrics,
            "Product Details",
            PanelContentType.JSON,
            recommendationPanelRequests,
            null
        )

        assertEquals(
            JsonHelper.toJson(recommendationPanelsRequest),
            "{\"device_id\":\"8ce554fe-a8b0-4d69-9196-06028c0ada4e\",\"customer_id_hash\":null,\"email_hash\":\"email_hash_value\",\"store_code\":\"GB\",\"currency_code\":\"GBP\",\"environment_code\":null,\"price_list\":{\"code\":\"test\",\"fallback_mode\":\"default\",\"alternatives\":[\"default\"]},\"metrics\":{\"data\":[{\"code\":\"delivery_country\",\"value\":\"GB\"}],\"non_interactive\":true},\"page_type\":\"Product Details\",\"content_type\":\"json\",\"panels\":[{\"id\":\"test_panel_code\",\"context\":{\"current\":{\"sku\":\"test_sku\",\"list_id\":\"\"},\"search_term\":\"\",\"skus\":[],\"list_ids\":[]},\"filter\":[{\"code\":\"filter_code\",\"operator\":\"eq\",\"value\":\"value\",\"type\":\"product\"}],\"attrs_to_include\":[\"designer\",\"is_sale\",\"small_image\"]}]}"
        )
    }

    @Test
    fun `test nullable serialization with nullable values in root object`() {
        val currentContext = RecommendationPanelRequest.RecommendationPanelRequestContext.CurrentContext(
            sku = "test_sku",
            listId = ""
        )
        val context = RecommendationPanelRequest.RecommendationPanelRequestContext(
            searchTerm = "",
            currentContext = currentContext,
            skus = emptyList(),
            listIds = emptyList()
        )

        val metrics = Metrics(
            nonInteractive = true,
            data = listOf(
                Metrics.Metric(
                    code = "delivery_country",
                    value = "GB"
                )
            )
        )

        val recommendationPanelRequests = listOf(
            RecommendationPanelRequest(
                id = "test_panel_code",
                context = context,
                filters = listOf(
                    RecommendationPanelRequest.RecommendationPanelRequestFilter(
                        code = "filter_code",
                        operator = "eq",
                        value = "value",
                    )
                ),
                attrsToInclude = listOf(
                    "designer",
                    "is_sale",
                    "small_image"
                ),
                variations = RecommendationPanelRequest.Variations(
                    true,
                    listOf(
                        "designer",
                        "is_sale",
                        "small_image"
                    )
                )
            )
        )

        val recommendationPanelsRequest = RecommendationPanelsRequest(
            "8ce554fe-a8b0-4d69-9196-06028c0ada4e",
            null,
            null,
            "default",
            "GBP",
            null,
            PriceList("test", PriceList.FallbackMode.DEFAULT, listOf("default")),
            metrics,
            "Product Details",
            PanelContentType.JSON,
            recommendationPanelRequests,
            null
        )

        val test = JsonHelper.toJson(recommendationPanelsRequest)

        assertEquals(
            JsonHelper.toJson(recommendationPanelsRequest),
            "{\"device_id\":\"8ce554fe-a8b0-4d69-9196-06028c0ada4e\",\"customer_id_hash\":null,\"store_code\":\"default\",\"currency_code\":\"GBP\",\"environment_code\":null,\"price_list\":{\"code\":\"test\",\"fallback_mode\":\"default\",\"alternatives\":[\"default\"]},\"metrics\":{\"data\":[{\"code\":\"delivery_country\",\"value\":\"GB\"}],\"non_interactive\":true},\"page_type\":\"Product Details\",\"content_type\":\"json\",\"panels\":[{\"id\":\"test_panel_code\",\"context\":{\"current\":{\"sku\":\"test_sku\",\"list_id\":\"\"},\"search_term\":\"\",\"skus\":[],\"list_ids\":[]},\"filter\":[{\"code\":\"filter_code\",\"operator\":\"eq\",\"value\":\"value\"}],\"attrs_to_include\":[\"designer\",\"is_sale\",\"small_image\"],\"variations\":{\"include\":true,\"attrs_to_include\":[\"designer\",\"is_sale\",\"small_image\"]}}]}"
        )
    }
}
