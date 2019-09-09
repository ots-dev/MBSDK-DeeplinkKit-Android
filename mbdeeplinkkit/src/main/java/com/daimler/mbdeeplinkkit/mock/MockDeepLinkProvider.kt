package com.daimler.mbdeeplinkkit.mock

import com.daimler.mbdeeplinkkit.DeepLinkProvider
import com.daimler.mbdeeplinkkit.common.DeepLink

internal class MockDeepLinkProvider : DeepLinkProvider {

    override fun isDeepLinkAvailable(id: String, finOrVin: String?): Boolean {
        return false
    }

    override fun getDeepLinkById(id: String, finOrVin: String?): DeepLink? {
        return null
    }

    override fun getDeepLinksForVin(vin: String): List<DeepLink>? {
        return emptyList()
    }

    override fun getAllDeepLinks(): List<DeepLink>? {
        return emptyList()
    }

    override fun getDeepLinksForApp(appId: String): List<DeepLink>? {
        return emptyList()
    }
}