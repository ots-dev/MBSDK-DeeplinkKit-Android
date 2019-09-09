package com.daimler.mbdeeplinkkit.persistence

import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLink
import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLinkParameter
import com.daimler.mbdeeplinkkit.persistence.model.RealmDeepLinkVehicle
import com.daimler.mbdeeplinkkit.persistence.model.RealmFamilyApp
import io.realm.annotations.RealmModule

@RealmModule(
    library = true,
    classes = [
        RealmFamilyApp::class,
        RealmDeepLink::class,
        RealmDeepLinkVehicle::class,
        RealmDeepLinkParameter::class
    ]
)
class MBDeepLinkKitRealmModule