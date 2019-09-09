package com.daimler.mbdeeplinkkit.persistence

import com.daimler.mbdeeplinkkit.common.DeepLink
import com.daimler.mbdeeplinkkit.common.FamilyApp

/**
 * Local cache for family apps and deep links.
 */
internal interface FamilyAppsCache {

    /**
     * Returns a list of all [FamilyApp]s or null if there are none.
     */
    fun loadFamilyApps(): List<FamilyApp>?

    /**
     * Returns the [FamilyApp] for the given [id] or null if it does not exist.
     *
     * @param id the unique ID of the app
     */
    fun loadFamilyAppById(id: String): FamilyApp?

    /**
     * Returns the [FamilyApp] for the [DeepLink] with the given [id] or null if it does not exist.
     *
     * @param id the id of the [DeepLink]
     */
    fun loadFamilyAppOfDeepLink(id: String): FamilyApp?

    /**
     * Creates or updates the given [FamilyApp]s.
     */
    fun cacheFamilyApps(familyApps: List<FamilyApp>)

    /**
     * Deletes the [FamilyApp] with the given [id] if it exists.
     *
     * @param id the unique ID of the [FamilyApp]
     */
    fun deleteFamilyApp(id: String)

    /**
     * Deletes the [DeepLink] with the given [id] if it exists.
     *
     * @param id the unique ID of the [DeepLink]
     */
    fun deleteDeepLink(id: String)

    /**
     * Deletes all entries of the local cache.
     */
    fun clear()
}