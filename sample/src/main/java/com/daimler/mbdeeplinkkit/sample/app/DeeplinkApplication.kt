package com.daimler.mbdeeplinkkit.sample.app

import android.app.Application
import android.os.Build
import com.daimler.mbdeeplinkkit.MBDeepLinkKit
import com.daimler.mbdeeplinkkit.DeepLinkServiceConfig
import com.daimler.mbdeeplinkkit.sample.BuildConfig
import com.daimler.mbloggerkit.MBLoggerKit
import com.daimler.mbloggerkit.PrinterConfig
import com.daimler.mbloggerkit.adapter.AndroidLogAdapter
import com.daimler.mbnetworkkit.MBNetworkKit
import com.daimler.mbnetworkkit.NetworkServiceConfig
import java.util.*

class DeeplinkApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MBLoggerKit.usePrinterConfig(
            PrinterConfig.Builder().apply {
                addAdapter(
                    AndroidLogAdapter.Builder().apply {
                        setLoggingEnabled(BuildConfig.DEBUG)
                    }.build()
                )
            }.build()
        )

        MBNetworkKit.init(
            NetworkServiceConfig.Builder("reference", "1.0", "1.0.0")
                .apply {
                    useOSVersion(Build.VERSION.RELEASE)
                    useLocale("de-DE")
                }.build()
        )

        val sessionId = UUID.randomUUID()

        MBDeepLinkKit.init(
            DeepLinkServiceConfig.Builder(
                this, BASE_URL, MBNetworkKit.headerService()
            ).apply {
                useSessionId(sessionId.toString())
            }.build()
        )
    }

    @Suppress("ConstantConditionIf")
    companion object {
        private const val USE_PROD = false
        private const val BASE_DOMAIN_INT = "bff-int.risingstars-int.daimler.com"
        private const val BASE_DOMAIN_PROD = "bff-prod.risingstars.daimler.com"
        private const val BASE_URL_INT = "https://$BASE_DOMAIN_INT"
        private const val BASE_URL_PROD = "https://$BASE_DOMAIN_PROD"

        val BASE_URL = if (USE_PROD) BASE_URL_PROD else BASE_URL_INT

        const val TOKEN = BuildConfig.TEST_TOKEN
    }
}