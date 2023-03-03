package com.recommend.sdk.recommendation

import com.recommend.sdk.Recommend
import com.recommend.sdk.core.data.ApiTask
import com.recommend.sdk.core.data.RequestTask
import com.recommend.sdk.core.data.api.ApiServiceBuilder
import com.recommend.sdk.core.data.listener.DataListener
import com.recommend.sdk.core.data.model.Metrics
import com.recommend.sdk.core.data.util.ApiHelper
import com.recommend.sdk.core.exception.RecommendException
import com.recommend.sdk.recommendation.data.api.ApiRecommendationService
import com.recommend.sdk.recommendation.data.api.request.RecommendationPanelRequest
import com.recommend.sdk.recommendation.data.api.request.RecommendationPanelsRequest
import com.recommend.sdk.recommendation.data.api.request.RecommendationPreviewPanelRequest
import com.recommend.sdk.recommendation.data.api.response.RecommendationPanelsErrorResponse
import com.recommend.sdk.recommendation.data.model.PanelContentType
import com.recommend.sdk.recommendation.data.model.RecommendationPanel

/**
 * Recommend recommendation
 *
 * @property recommend
 * @constructor Create Recommend recommendation
 */
class RecommendRecommendation(
    private val recommend: Recommend
) {
    private val apiRecommendationService: ApiRecommendationService = ApiServiceBuilder.getService(
        recommend.config.accountId,
        recommend.config.apiHost,
        ApiHelper(recommend.context),
        ApiRecommendationService::class.java,
        recommend.getLogger()
    )

    fun getRecommendPanel(
        onLoad: (panels: List<RecommendationPanel>) -> Unit,
        onError: ((error: Throwable) -> Unit)? = null,
        metrics: Metrics? = null,
        pageType: String? = null,
        contentType: PanelContentType = PanelContentType.JSON,
        panelRequests: List<RecommendationPanelRequest>? = null,
        previewPanelRequest: RecommendationPreviewPanelRequest? = null
    ) {
        val activityEnvironment = recommend.config.environment

        val storeCode = activityEnvironment.store
        if (storeCode == null) {
            onError?.let { it(RecommendException("Store code is null in environment. Set store code for getting recommend panel.")) }
            return
        }

        val currencyCode = activityEnvironment.currency
        if (currencyCode == null) {
            onError?.let { it(RecommendException("Currency code is null in environment. Set currency code for getting recommend panel.")) }
            return
        }

        val priceList = activityEnvironment.priceList
        if (priceList == null) {
            onError?.let { it(RecommendException("Price list is null in environment. Set price list for getting recommend panel.")) }
            return
        }

        recommend.getDeviceId { deviceId ->
            recommend.getApiManager().processTask(
                ApiTask(
                    RequestTask(
                        apiRecommendationService.getPanel(
                            RecommendationPanelsRequest(
                                deviceId,
                                activityEnvironment.customerIdHash,
                                activityEnvironment.customerEmailHash,
                                storeCode,
                                currencyCode,
                                activityEnvironment.environment,
                                priceList,
                                metrics,
                                pageType,
                                contentType,
                                panelRequests,
                                previewPanelRequest
                            )
                        ),
                        dataListener = DataListener(
                            { response, _ ->
                                if (response != null) {
                                    onLoad(response.result)
                                } else {
                                    onError?.let { it(RecommendException("Recommend panel response is empty")) }
                                }
                            },
                            { error, _ ->
                                onError?.let { it(error) }
                            }
                        ),
                        inTurn = false
                    ),
                    apiErrorResponseType = RecommendationPanelsErrorResponse::class.java
                )
            )
        }
    }
}
