package com.daimler.mbdeeplinkkit

import android.content.Context
import com.daimler.mbdeeplinkkit.implementation.AndroidIntentService
import com.daimler.mbdeeplinkkit.implementation.CachedFamilyAppsService
import com.daimler.mbdeeplinkkit.network.RetrofitFamilyAppsService
import com.daimler.mbdeeplinkkit.network.RetrofitServiceProvider
import com.daimler.mbdeeplinkkit.persistence.*
import com.daimler.mbdeeplinkkit.persistence.implementation.RealmDeepLinkProvider
import com.daimler.mbdeeplinkkit.persistence.implementation.RealmFamilyAppsCache
import com.daimler.mbnetworkkit.header.HeaderService
import com.daimler.mbrealmkit.MBRealmKit
import com.daimler.mbrealmkit.RealmServiceConfig
import java.util.*

class DeepLinkServiceConfig private constructor(
    internal val familyAppsService: FamilyAppsService,
    internal val deepLinkProvider: DeepLinkProvider,
    internal val familyAppsCache: FamilyAppsCache,
    internal val intentService: IntentService
) {

    class Builder(
        private val context: Context,
        private val networkUrl: String,
        private val headerService: HeaderService
    ) {

        private var sessionId: String? = null

        fun useSessionId(sessionId: String): Builder {
            this.sessionId = sessionId
            return this
        }

        fun build(): DeepLinkServiceConfig {
            setupRealm()
            val realm = MBRealmKit.realm(ID_ENCRYPTED_REALM)
            val retrofitAppsService: FamilyAppsService = RetrofitFamilyAppsService(
                RetrofitServiceProvider.createDeepLinkApi(context, networkUrl, headerService),
                sessionId ?: UUID.randomUUID().toString()
            )
            val appsCache: FamilyAppsCache = RealmFamilyAppsCache(realm)
            val appsService: FamilyAppsService = CachedFamilyAppsService(appsCache, retrofitAppsService)
            val deepLinkProvider: DeepLinkProvider = RealmDeepLinkProvider(realm)
            val intentService: IntentService = AndroidIntentService(appsCache, deepLinkProvider)
            return DeepLinkServiceConfig(
                appsService,
                deepLinkProvider,
                appsCache,
                intentService
            )
        }

        private fun setupRealm() {
            MBRealmKit.createRealmInstance(
                ID_ENCRYPTED_REALM,
                RealmServiceConfig.Builder(
                    context,
                    ENCRYPTED_REALM_SCHEMA_VERSION,
                    MBDeepLinkKitRealmModule()
                ).apply {
                    useDbName(ENCRYPTED_REALM_FILE_NAME)
                    encrypt()
                }.build()
            )
        }
    }
}