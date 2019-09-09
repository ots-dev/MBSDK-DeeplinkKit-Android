package com.daimler.mbdeeplinkkit.network.model

import com.google.gson.annotations.SerializedName

internal data class ApiApp(
    @SerializedName("appBar") val appBar: Boolean,
    @SerializedName("description") val description: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("marketingLink") val marketingLink: String?,
    @SerializedName("name") val name: String,
    @SerializedName("scheme") val scheme: String,
    @SerializedName("storeLink") val storeLink: String?,
    @SerializedName("deeplinks") val deepLinks: List<ApiDeepLink>?
)