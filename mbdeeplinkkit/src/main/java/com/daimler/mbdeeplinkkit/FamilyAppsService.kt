package com.daimler.mbdeeplinkkit

import com.daimler.mbdeeplinkkit.common.FamilyApp
import com.daimler.mbnetworkkit.networking.RequestError
import com.daimler.mbnetworkkit.networking.ResponseError
import com.daimler.mbnetworkkit.task.FutureTask

/**
 * Service calls for family apps.
 */
interface FamilyAppsService {

    /**
     * Fetches all family apps from the network and cached them afterwards. Returns the local cached apps if the network
     * request was not successful.
     */
    fun fetchAppsAndDeepLinks(jwtToken: String): FutureTask<List<FamilyApp>, ResponseError<out RequestError>?>

    /**
     * Returns a list of locally cached [FamilyApp]s or null if there are none.
     */
    fun loadAppsAndDeepLinks(): List<FamilyApp>?
}