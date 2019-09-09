package com.daimler.mbdeeplinkkit.persistence.model

import io.realm.RealmObject

internal open class RealmDeepLinkVehicle : RealmObject() {

    var finOrVin: String? = null

    companion object {

        const val FIELD_VIN = "finOrVin"
    }
}