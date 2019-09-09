package com.daimler.mbdeeplinkkit.network.model

import com.google.gson.annotations.SerializedName

internal data class ApiDeepLink(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("icon") val iconUrl: String?,
    @SerializedName("link") val link: String,
    @SerializedName("name") val name: String,
    @SerializedName("vehicles") val vehicles: List<String>?
)