package com.daimler.mbdeeplinkkit.implementation

import com.daimler.mbdeeplinkkit.FamilyAppsService
import com.daimler.mbdeeplinkkit.common.FamilyApp
import com.daimler.mbdeeplinkkit.persistence.FamilyAppsCache
import com.daimler.mbnetworkkit.networking.RequestError
import com.daimler.mbnetworkkit.networking.ResponseError
import com.daimler.mbnetworkkit.task.FutureTask
import com.daimler.mbnetworkkit.task.TaskObject

internal class CachedFamilyAppsService(
    private val cache: FamilyAppsCache,
    private val delegate: FamilyAppsService
) : FamilyAppsService {

    override fun fetchAppsAndDeepLinks(jwtToken: String): FutureTask<List<FamilyApp>, ResponseError<out RequestError>?> {
        val deferredTask = TaskObject<List<FamilyApp>, ResponseError<out RequestError>?>()
        delegate.fetchAppsAndDeepLinks(jwtToken)
            .onComplete {
                updateCache(it)
                deferredTask.complete(it)
            }.onFailure { error ->
                cache.loadFamilyApps()?.let {
                    deferredTask.complete(it)
                } ?: deferredTask.fail(error)
            }
        return deferredTask.futureTask()
    }

    override fun loadAppsAndDeepLinks(): List<FamilyApp>? {
        return cache.loadFamilyApps()
    }

    private fun updateCache(newApps: List<FamilyApp>) {
        deleteRemovedApps(newApps, cache.loadFamilyApps() ?: emptyList())
        cache.cacheFamilyApps(newApps)
    }

    private fun deleteRemovedApps(newApps: List<FamilyApp>, currentApps: List<FamilyApp>) {
        currentApps
            .filter { cachedApp ->
                // Filter for all apps that are present in the cached list but not in the new list.
                newApps.none {
                    it.appId == cachedApp.appId
                }
            }.forEach {
                cache.deleteFamilyApp(it.appId)
            }
    }
}