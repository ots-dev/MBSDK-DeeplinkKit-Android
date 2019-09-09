package com.daimler.mbdeeplinkkit.persistence.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

internal open class RealmDeepLink : RealmObject() {

    @PrimaryKey
    var id: String = ""

    var title: String? = null
    var scheme: String? = null
    var host: String? = null
    var iconUrl: String? = null
    var vehicles: RealmList<RealmDeepLinkVehicle>? = null
    var parameters: RealmList<RealmDeepLinkParameter>? = null

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_VEHICLES = "vehicles"
        const val FIELD_VEHICLE_VIN = "$FIELD_VEHICLES.${RealmDeepLinkVehicle.FIELD_VIN}"
    }
}