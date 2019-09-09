package com.daimler.mbdeeplinkkit

import com.daimler.mbdeeplinkkit.common.DeepLink

/**
 * Provider interface for deep link related tasks.
 * NOTE: This provider works offline. Make sure to update the locally cached data.
 */
interface DeepLinkProvider {

    /**
     * Returns true if the deep link for the given [id] and [finOrVin] is available.
     *
     * @param id the id of the deep link
     * @param finOrVin the FIN or VIN of a vehicle
     *
     * @return If [finOrVin] is null this method returns true if there is a [DeepLink] with the
     * given [id]. If [finOrVin] is not null, true is only returned if this [DeepLink] is eligible
     * for the vehicle.
     */
    fun isDeepLinkAvailable(id: String, finOrVin: String?): Boolean

    /**
     * Returns the deep link with the given [id] and for the given [finOrVin].
     *
     * @param id the id of the deep link
     * @param finOrVin the FIN or VIN of a vehicle
     *
     * @return If [finOrVin] is null this method returns a [DeepLink] with the given [id] if
     * such a [DeepLink] exists. If [finOrVin] is not null this method returns this [DeepLink]
     * only if it is eligible for the vehicle.
     */
    fun getDeepLinkById(id: String, finOrVin: String?): DeepLink?

    /**
     * Returns all deep links available for the given vehicle.
     *
     * @param vin the FIN or VIN of the vehicle
     */
    fun getDeepLinksForVin(vin: String): List<DeepLink>?

    /**
     * Returns all deep links.
     */
    fun getAllDeepLinks(): List<DeepLink>?

    /**
     * Returns all deep links for the given family app.
     *
     * @param appId the unique id of the family app
     */
    fun getDeepLinksForApp(appId: String): List<DeepLink>?
}