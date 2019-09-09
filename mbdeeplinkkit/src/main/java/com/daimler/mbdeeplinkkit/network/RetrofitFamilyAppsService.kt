package com.daimler.mbdeeplinkkit.network

import android.net.Uri
import com.daimler.mbdeeplinkkit.FamilyAppsService
import com.daimler.mbdeeplinkkit.common.DeepLink
import com.daimler.mbdeeplinkkit.common.DeepLinkVehicle
import com.daimler.mbdeeplinkkit.common.FamilyApp
import com.daimler.mbdeeplinkkit.network.model.ApiApp
import com.daimler.mbdeeplinkkit.network.model.ApiDeepLink
import com.daimler.mbnetworkkit.networking.RequestError
import com.daimler.mbnetworkkit.networking.ResponseError
import com.daimler.mbnetworkkit.networking.RetrofitTask
import com.daimler.mbnetworkkit.networking.defaultErrorMapping
import com.daimler.mbnetworkkit.task.FutureTask
import com.daimler.mbnetworkkit.task.TaskObject
import java.util.*

internal class RetrofitFamilyAppsService(
    private val deepLinkApi: DeepLinkApi,
    private val sessionId: String
) : FamilyAppsService {

    override fun fetchAppsAndDeepLinks(jwtToken: String): FutureTask<List<FamilyApp>, ResponseError<out RequestError>?> {
        val deferredTask = TaskObject<List<FamilyApp>, ResponseError<out RequestError>?>()
        val call = deepLinkApi.fetchDeepLinks(
            jwtToken,
            sessionId,
            UUID.randomUUID().toString()
        )
        val callback = RetrofitTask<Map<String, ApiApp>>()
        callback
            .onComplete {
                deferredTask.complete(mapApiAppsMapToFamilyApps(it))
            }.onFailure {
                deferredTask.fail(mapFetchAppsError(it))
            }
        call.enqueue(callback)
        return deferredTask.futureTask()
    }

    override fun loadAppsAndDeepLinks(): List<FamilyApp>? {
        return null
    }

    private fun mapApiAppsMapToFamilyApps(apiApps: Map<String, ApiApp>): List<FamilyApp> =
        apiApps.map {
            mapApiAppToFamilyApp(it.key, it.value)
        }

    private fun mapApiAppToFamilyApp(id: String, apiApp: ApiApp): FamilyApp =
        FamilyApp(
            id,
            apiApp.name,
            apiApp.description.orEmpty(),
            apiApp.scheme,
            apiApp.icon,
            apiApp.storeLink,
            apiApp.marketingLink,
            apiApp.appBar,
            apiApp.deepLinks?.let { mapApiDeepLinksToDeepLinks(apiApp.scheme, it) } ?: emptyList()
        )

    private fun mapApiDeepLinksToDeepLinks(appScheme: String, apiDeepLinks: List<ApiDeepLink>): List<DeepLink> {
        return apiDeepLinks.map { apiDeepLink ->
            val uri = createUriFromApiLink(apiDeepLink.link, appScheme)
            DeepLink(
                apiDeepLink.identifier,
                apiDeepLink.name,
                uri.scheme ?: appScheme,
                uri.authority,
                apiDeepLink.iconUrl,
                apiDeepLink.vehicles?.map(::mapApiVehicleToDeepLinkVehicle) ?: emptyList()
            ).apply {
                uri.queryParametersAsMap()?.let {
                    addAllParameters(it)
                }
            }
        }
    }

    private fun createUriFromApiLink(link: String, appScheme: String): Uri {
        val linkUri = Uri.parse(link)
        return if (linkUri.scheme != null) {
            linkUri
        } else {
            createUriWithScheme(link, appScheme)
        }
    }

    /**
     * Returns the concatenation of [scheme] and [link] if [scheme] is not blank.
     * Adds the scheme separator "://" to [scheme] if it is not present.
     */
    private fun createUriWithScheme(link: String, scheme: String): Uri {
        return if (scheme.isBlank()) {
            Uri.parse(link)
        } else {
            Uri.parse("${scheme.toUriScheme()}$link")
        }
    }

    private fun mapApiVehicleToDeepLinkVehicle(finOrVin: String) =
        DeepLinkVehicle(finOrVin)

    private fun mapFetchAppsError(error: Throwable?): ResponseError<out RequestError>? {
        return defaultErrorMapping(error)
    }

    private fun Uri.queryParametersAsMap(): Map<String, String>? =
        try {
            queryParameterNames.map {
                it to getQueryParameter(it)
            }.toMap()
        } catch (e: Exception) {
            null
        }

    private fun String.isUriScheme(): Boolean = this.contains("://")

    private fun String.toUriScheme(): String = if (isUriScheme()) this else "$this://"
}