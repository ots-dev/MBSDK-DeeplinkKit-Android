package com.daimler.mbdeeplinkkit.network

import com.daimler.mbdeeplinkkit.network.model.ApiApp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

internal interface DeepLinkApi {

    private companion object {

        private const val PATH_VERSION = "/v1"
        private const val PATH_DEEP_LINK = "/deeplinks"

        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_SESSION_ID = "X-SessionId"
        private const val HEADER_TRACKING_ID = "X-TrackingId"
    }

    @GET("$PATH_VERSION$PATH_DEEP_LINK")
    fun fetchDeepLinks(
        @Header(HEADER_AUTHORIZATION) jwtToken: String,
        @Header(HEADER_SESSION_ID) sessionId: String,
        @Header(HEADER_TRACKING_ID) trackingId: String
    ): Call<Map<String, ApiApp>>
}