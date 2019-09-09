package com.daimler.mbdeeplinkkit.persistence.implementation

import com.daimler.mbdeeplinkkit.common.DeepLink
import com.daimler.mbdeeplinkkit.common.DeepLinkVehicle
import com.daimler.mbdeeplinkkit.common.FamilyApp
import com.daimler.mbdeeplinkkit.persistence.FamilyAppsCache
import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLink
import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLinkParameter
import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLinkVehicle
import com.daimler.mbdeeplinkkit.persistence.model.RealmFamilyApp
import com.daimler.mbloggerkit.MBLoggerKit
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.createObject
import io.realm.kotlin.delete

internal class RealmFamilyAppsCache(realm: Realm) : BaseDeepLinkRealmCache(realm), FamilyAppsCache {

    override fun loadFamilyApps(): List<FamilyApp>? {
        return familyApps(realm)
            ?.takeIf { it.isNotEmpty() }
            ?.let(::mapRealmFamilyAppsToFamilyApps)
    }

    override fun loadFamilyAppById(id: String): FamilyApp? {
        return familyAppById(realm, id)?.let(::mapRealmFamilyAppToFamilyApp)
    }

    override fun loadFamilyAppOfDeepLink(id: String): FamilyApp? {
        return familyAppForDeepLinkId(realm, id)?.let(::mapRealmFamilyAppToFamilyApp)
    }

    override fun cacheFamilyApps(familyApps: List<FamilyApp>) {
        realm.executeTransactionAsync { realm ->
            familyApps.forEach { familyApp ->
                val appId = familyApp.appId

                var current = familyAppById(realm, appId)
                if (current == null) {
                    MBLoggerKit.d("Creating app $appId in cache.")
                    current = realm.createObject(appId)
                } else {
                    MBLoggerKit.d("Updating app $appId in cache.")
                }

                current.apply {
                    name = familyApp.name
                    description = familyApp.description
                    scheme = familyApp.scheme
                    iconUrl = familyApp.iconUrl
                    storeLink = familyApp.storeLink
                    marketingLink = familyApp.marketingLink
                    showInMenuBar = familyApp.showInMenuBar
                    deepLinks = createOrUpdateDeepLinks(realm, deepLinks, familyApp.deepLinks)

                    realm.copyToRealmOrUpdate(this)
                }
            }
        }
    }

    override fun deleteFamilyApp(id: String) {
        realm.executeTransaction {
            familyAppById(it, id)?.cascadeDelete()
        }
    }

    override fun deleteDeepLink(id: String) {
        realm.executeTransaction {
            deepLinkById(it, id)?.cascadeDelete()
        }
    }

    override fun clear() {
        realm.executeTransactionAsync {
            it.delete<RealmDeepLinkParameter>()
            it.delete<RealmDeepLinkVehicle>()
            it.delete<RealmDeepLink>()
            it.delete<RealmFamilyApp>()
        }
    }

    private fun createOrUpdateDeepLinks(
        realm: Realm,
        currentRealmLinks: RealmList<RealmDeepLink>?,
        deepLinks: List<DeepLink>
    ): RealmList<RealmDeepLink> {
        currentRealmLinks?.forEach { realmLink ->
            if (deepLinks.none { realmLink.id == it.id }) {
                realmLink.cascadeDelete()
            }
        }

        val result = RealmList<RealmDeepLink>()
        deepLinks.forEach { link ->
            var current = deepLinkById(realm, link.id)
            if (current == null) {
                MBLoggerKit.d("Creating DeepLink ${link.id} in cache.")
                current = realm.createObject(link.id)
            } else {
                MBLoggerKit.d("Updating DeepLink ${link.id} in cache.")
            }
            current.apply {
                title = link.title
                scheme = link.scheme
                host = link.host
                iconUrl = link.iconUrl
                vehicles = createOrUpdateRealmVehicles(realm, vehicles, link.vehicles)
                parameters = createOrUpdateRealmParameters(realm, parameters, link.parameters)

                realm.copyToRealmOrUpdate(this)
                result.add(this)
            }
        }
        return result
    }

    private fun createOrUpdateRealmVehicles(
        realm: Realm,
        currentVehicles: RealmList<RealmDeepLinkVehicle>?,
        vehicles: List<DeepLinkVehicle>
    ): RealmList<RealmDeepLinkVehicle>? {
        currentVehicles?.deleteAllFromRealm()
        return vehicles
            .takeIf { it.isNotEmpty() }
            ?.map {
                realm.createObject<RealmDeepLinkVehicle>().apply {
                    finOrVin = it.finOrVin
                }
            }?.toRealmList()
    }

    private fun createOrUpdateRealmParameters(
        realm: Realm,
        currentParameters: RealmList<RealmDeepLinkParameter>?,
        parameters: Map<String, String>
    ): RealmList<RealmDeepLinkParameter>? {
        currentParameters?.deleteAllFromRealm()
        return parameters
            .takeIf { it.isNotEmpty() }
            ?.map {
                realm.createObject<RealmDeepLinkParameter>().apply {
                    key = it.key
                    value = it.value
                }
            }?.toRealmList()
    }
}