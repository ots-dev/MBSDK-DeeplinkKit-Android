package com.daimler.mbdeeplinkkit.mock

import android.os.Handler
import com.daimler.mbdeeplinkkit.FamilyAppsService
import com.daimler.mbdeeplinkkit.common.DeepLink
import com.daimler.mbdeeplinkkit.common.DeepLinkVehicle
import com.daimler.mbdeeplinkkit.common.FamilyApp
import com.daimler.mbdeeplinkkit.persistence.FamilyAppsCache
import com.daimler.mbnetworkkit.networking.RequestError
import com.daimler.mbnetworkkit.networking.ResponseError
import com.daimler.mbnetworkkit.task.FutureTask
import com.daimler.mbnetworkkit.task.TaskObject

internal class MockFamilyAppsService(
    private val cache: FamilyAppsCache
) : FamilyAppsService {

    override fun fetchAppsAndDeepLinks(jwtToken: String): FutureTask<List<FamilyApp>, ResponseError<out RequestError>?> {
        val result = listOf(app1(), app2())
        cache.cacheFamilyApps(result)
        return TaskObject<List<FamilyApp>, ResponseError<out RequestError>?>().apply {
            Handler().post { complete(result) }
        }.futureTask()
    }

    override fun loadAppsAndDeepLinks(): List<FamilyApp>? {
        return cache.loadFamilyApps()
    }

    private fun app1() =
        FamilyApp(
            "app.id.1",
            "App 1",
            "Description of App1",
            "http",
            null,
            "www.google.de",
            "www.google.de",
            false,
            listOf(deepLink1())
        )

    private fun app2() =
        FamilyApp(
            "app.id.2",
            "App 2",
            "Description of App2",
            "http",
            null,
            "www.google.de",
            "www.google.de",
            false,
            listOf(deepLink2(), deepLink3())
        )

    private fun deepLink1() =
        DeepLink(
            "dl.id.1",
            "DeepLink 1",
            "http",
            null,
            null,
            listOf(
                DeepLinkVehicle("123"),
                DeepLinkVehicle("456")
            ),
            mutableMapOf(
                "lat" to "50",
                "long" to "50"
            )
        )

    private fun deepLink2() =
        DeepLink(
            "dl.id.2",
            "DeepLink 2",
            "http",
            "host2",
            null,
            listOf(
                DeepLinkVehicle("123"),
                DeepLinkVehicle("111")
            )
        )

    private fun deepLink3() =
        DeepLink(
            "dl.id.3",
            "DeepLink 3",
            "http",
            null,
            null,
            listOf(
                DeepLinkVehicle("123"),
                DeepLinkVehicle("111")
            )
        )
}