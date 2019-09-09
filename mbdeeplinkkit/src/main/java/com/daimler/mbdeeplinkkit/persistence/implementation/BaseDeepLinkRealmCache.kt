package com.daimler.mbdeeplinkkit.persistence.implementation

import com.daimler.mbdeeplinkkit.common.DeepLink
import com.daimler.mbdeeplinkkit.common.DeepLinkVehicle
import com.daimler.mbdeeplinkkit.common.FamilyApp
import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLink
import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLinkParameter
import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLinkVehicle
import com.daimler.mbdeeplinkkit.persistence.model.RealmFamilyApp
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.kotlin.where

internal abstract class BaseDeepLinkRealmCache(protected val realm: Realm) {

    protected fun familyApps(realm: Realm): List<RealmFamilyApp>? =
        realm
            .where<RealmFamilyApp>()
            .findAll()

    protected fun familyAppById(realm: Realm, id: String): RealmFamilyApp? =
        realm.where<RealmFamilyApp>()
            .equalTo(RealmFamilyApp.FIELD_ID, id)
            .findFirst()

    protected fun familyAppForDeepLinkId(realm: Realm, deepLinkId: String): RealmFamilyApp? =
        realm.where<RealmFamilyApp>()
            .contains(RealmFamilyApp.FIELD_DEEP_LINK_ID, deepLinkId)
            .findFirst()

    protected fun deepLinks(realm: Realm): List<RealmDeepLink>? =
        realm
            .where<RealmDeepLink>()
            .findAll()

    protected fun deepLinkById(realm: Realm, id: String): RealmDeepLink? =
        realm.where<RealmDeepLink>()
            .equalTo(RealmDeepLink.FIELD_ID, id)
            .findFirst()

    protected fun deepLinkByIdAndVin(realm: Realm, id: String, finOrVin: String): RealmDeepLink? =
        realm.where<RealmDeepLink>()
            .equalTo(RealmDeepLink.FIELD_ID, id)
            .contains(RealmDeepLink.FIELD_VEHICLE_VIN, finOrVin)
            .findFirst()

    protected fun deepLinksForVin(realm: Realm, finOrVin: String): List<RealmDeepLink>? =
        realm.where<RealmDeepLink>()
            .contains(RealmDeepLink.FIELD_VEHICLE_VIN, finOrVin)
            .findAll()

    protected fun deepLinksForApp(realm: Realm, appId: String): List<RealmDeepLink>? =
        realm.where<RealmFamilyApp>()
            .equalTo(RealmFamilyApp.FIELD_ID, appId)
            .findFirst()
            ?.deepLinks

    protected fun RealmFamilyApp.cascadeDelete() {
        deleteNestedObjectsFromRealm()
        deleteFromRealm()
    }

    private fun RealmFamilyApp.deleteNestedObjectsFromRealm() {
        deepLinks?.forEach { it.deleteNestedObjectsFromRealm() }
        deepLinks?.deleteAllFromRealm()
    }

    protected fun RealmDeepLink.cascadeDelete() {
        deleteNestedObjectsFromRealm()
        deleteFromRealm()
    }

    private fun RealmDeepLink.deleteNestedObjectsFromRealm() {
        parameters?.deleteAllFromRealm()
        vehicles?.deleteAllFromRealm()
    }

    protected fun mapRealmFamilyAppsToFamilyApps(realmApps: List<RealmFamilyApp>): List<FamilyApp> {
        return realmApps.map(::mapRealmFamilyAppToFamilyApp)
    }

    protected fun mapRealmFamilyAppToFamilyApp(realmApp: RealmFamilyApp) =
        FamilyApp(
            realmApp.id,
            realmApp.name.orEmpty(),
            realmApp.description.orEmpty(),
            realmApp.scheme.orEmpty(),
            realmApp.iconUrl,
            realmApp.storeLink,
            realmApp.marketingLink,
            realmApp.showInMenuBar == true,
            realmApp.deepLinks?.let(::mapRealmDeepLinksToDeepLinks) ?: emptyList()
        )

    protected fun mapRealmDeepLinksToDeepLinks(realmLinks: List<RealmDeepLink>): List<DeepLink> {
        return realmLinks.map(::mapRealmDeepLinkToDeepLink)
    }

    protected fun mapRealmDeepLinkToDeepLink(realmLink: RealmDeepLink) =
        DeepLink(
            realmLink.id,
            realmLink.title.orEmpty(),
            realmLink.scheme.orEmpty(),
            realmLink.host,
            realmLink.iconUrl,
            realmLink.vehicles?.let(::mapRealmVehiclesToVehicles) ?: emptyList(),
            realmLink.parameters?.let(::mapRealmParametersToParameters)?.toMutableMap()
                ?: mutableMapOf()
        )

    private fun mapRealmVehiclesToVehicles(realmVehicles: List<RealmDeepLinkVehicle>): List<DeepLinkVehicle> {
        return realmVehicles.map(::mapRealmVehicleToVehicle)
    }

    private fun mapRealmVehicleToVehicle(realmDeepLinkVehicle: RealmDeepLinkVehicle) =
        DeepLinkVehicle(realmDeepLinkVehicle.finOrVin.orEmpty())

    private fun mapRealmParametersToParameters(realmParameters: List<RealmDeepLinkParameter>?): Map<String, String>? {
        return realmParameters
            ?.map(::mapRealmParameterToParameter)
            ?.toMap()
    }

    private fun mapRealmParameterToParameter(realmParameter: RealmDeepLinkParameter) =
        Pair(realmParameter.key.orEmpty(), realmParameter.value.orEmpty())

    protected inline fun <reified T : RealmObject> List<T>.toRealmList(): RealmList<T> {
        val realmList = RealmList<T>()
        forEach { realmList.add(it) }
        return realmList
    }
}