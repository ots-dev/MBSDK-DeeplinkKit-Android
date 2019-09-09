package com.daimler.mbdeeplinkkit.common

/**
 * Describes a vehicle for a specific deep link.
 */
data class DeepLinkVehicle(

    /** The FIN or VIN of the vehicle. */
    val finOrVin: String
)