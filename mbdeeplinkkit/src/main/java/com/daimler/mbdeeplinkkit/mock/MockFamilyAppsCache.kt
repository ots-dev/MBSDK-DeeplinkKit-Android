package com.daimler.mbdeeplinkkit.mock

import com.daimler.mbcommonkit.extensions.replace
import com.daimler.mbdeeplinkkit.common.FamilyApp
import com.daimler.mbdeeplinkkit.persistence.FamilyAppsCache

internal class MockFamilyAppsCache : FamilyAppsCache {

    private val cache = mutableListOf<FamilyApp>()

    override fun loadFamilyApps(): List<FamilyApp>? {
        return cache.takeIf { it.isNotEmpty() }
    }

    override fun loadFamilyAppById(id: String): FamilyApp? {
        return cache.find { it.appId == id }
    }

    override fun loadFamilyAppOfDeepLink(id: String): FamilyApp? {
        return cache.find { app -> app.deepLinks.any { it.id == id } }
    }

    override fun cacheFamilyApps(familyApps: List<FamilyApp>) {
        familyApps.forEach { newApp ->
            cache.apply {
                find { it.appId == newApp.appId }?.let {
                    replace(it, newApp)
                } ?: add(newApp)
            }
        }
    }

    override fun deleteFamilyApp(id: String) {
        cache.removeAll { it.appId == id }
    }

    override fun deleteDeepLink(id: String) {
        val result = cache.map { app ->
            val mutableLinks = ArrayList(app.deepLinks)
            mutableLinks.removeAll { it.id == id }
            app.copy(deepLinks = mutableLinks)
        }
        cacheFamilyApps(result)
    }

    override fun clear() {
        cache.clear()
    }
}