package com.daimler.mbdeeplinkkit.persistence.model

import io.realm.RealmObject

internal open class RealmDeepLinkParameter : RealmObject() {

    var key: String? = null
    var value: String? = null
}