package com.recommend.sdk.recommendation.data.api.request

import com.google.gson.annotations.SerializedName
import com.recommend.sdk.core.data.model.Metrics
import com.recommend.sdk.core.data.model.PriceList
import com.recommend.sdk.core.util.SerializeNull
import com.recommend.sdk.recommendation.data.model.PanelContentType

/**
 * Recommendation panel request
 *
 * @property deviceId Browser identifier
 * @property customerIdHash Customer identifier hash
 * @property storeCode Store code
 * @property currencyCode Currency code
 * @property environmentCode Environment code
 * @property priceList
 * @property metrics
 * @property pageType
 * @property contentType
 * @property panels
 * @property previewPanel
 * @constructor Create Recommendation panel request
 */
data class RecommendationPanelsRequest(
    @SerializedName("device_id") val deviceId: String,
    @SerializeNull @SerializedName("customer_id_hash") val customerIdHash: String?,
    @SerializedName("email_hash") val customerEmailHash: String?,
    @SerializedName("store_code") val storeCode: String,
    @SerializedName("currency_code") val currencyCode: String,
    @SerializeNull @SerializedName("environment_code") val environmentCode: String?,
    @SerializedName("price_list") val priceList: PriceList,
    val metrics: Metrics?,
    @SerializedName("page_type") val pageType: String?,
    @SerializedName("content_type") val contentType: PanelContentType,
    val panels: List<RecommendationPanelRequest>?,
    @SerializedName("preview_panel") val previewPanel: RecommendationPreviewPanelRequest?
)
