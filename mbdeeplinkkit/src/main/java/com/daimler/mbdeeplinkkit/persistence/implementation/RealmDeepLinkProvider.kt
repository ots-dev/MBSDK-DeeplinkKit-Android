package com.daimler.mbdeeplinkkit.persistence.implementation

import com.daimler.mbdeeplinkkit.DeepLinkProvider
import com.daimler.mbdeeplinkkit.common.DeepLink
import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLink
import io.realm.Realm
import io.realm.kotlin.where

internal class RealmDeepLinkProvider(
    realm: Realm
) : BaseDeepLinkRealmCache(realm), DeepLinkProvider {

    override fun isDeepLinkAvailable(id: String, finOrVin: String?): Boolean {
        val query = realm.where<RealmDeepLink>()
            .equalTo(RealmDeepLink.FIELD_ID, id).apply {
                finOrVin?.takeIf { !it.isBlank() }?.let {
                    contains(RealmDeepLink.FIELD_VEHICLE_VIN, finOrVin)
                }
            }
        return query.count() > 0
    }

    override fun getDeepLinkById(id: String, finOrVin: String?): DeepLink? {
        val query: () -> RealmDeepLink? = finOrVin?.let {
            { deepLinkByIdAndVin(realm, id, it) }
        } ?: { deepLinkById(realm, id) }
        return query.invoke()?.let(::mapRealmDeepLinkToDeepLink)
    }

    override fun getDeepLinksForVin(vin: String): List<DeepLink>? {
        return deepLinksForVin(realm, vin)
            ?.takeIf { it.isNotEmpty() }
            ?.let(::mapRealmDeepLinksToDeepLinks)
    }

    override fun getAllDeepLinks(): List<DeepLink>? {
        return deepLinks(realm)
            ?.takeIf { it.isNotEmpty() }
            ?.let(::mapRealmDeepLinksToDeepLinks)
    }

    override fun getDeepLinksForApp(appId: String): List<DeepLink>? {
        return deepLinksForApp(realm, appId)
            ?.let(::mapRealmDeepLinksToDeepLinks)
    }
}