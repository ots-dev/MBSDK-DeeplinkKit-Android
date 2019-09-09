package com.daimler.mbdeeplinkkit.persistence.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

internal open class RealmFamilyApp : RealmObject() {

    @PrimaryKey
    var id: String = ""

    var name: String? = null
    var description: String? = null
    var scheme: String? = null
    var iconUrl: String? = null
    var storeLink: String? = null
    var marketingLink: String? = null
    var showInMenuBar: Boolean? = null
    var deepLinks: RealmList<RealmDeepLink>? = null

    companion object {
        const val FIELD_ID = "id"
        const val FIELD_DEEP_LINK_ID = "deepLinks.${RealmDeepLink.FIELD_ID}"
    }
}