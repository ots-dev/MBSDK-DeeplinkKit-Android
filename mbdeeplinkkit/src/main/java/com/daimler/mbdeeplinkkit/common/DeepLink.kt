package com.daimler.mbdeeplinkkit.common

import android.net.Uri

/** Represents a deep link used to open other family apps via intents. */
data class DeepLink(

    /** The unique ID for this deep link. */
    val id: String,

    /** The localized name for this deep link. */
    val title: String,

    /** The url scheme for the app this deep link points to (e.g. myApp://). */
    val scheme: String,

    /** The host (authority) of this deep link (e.g. myApp://<myauthority>). */
    val host: String?,

    /** The url to the icon for this deep link. */
    val iconUrl: String?,

    /** Contains the vehicles this DeepLink can be applied to. */
    val vehicles: List<DeepLinkVehicle>,

    private val _parameters: MutableMap<String, String> = mutableMapOf()
) {

    /**
     * A map that contains key-value pairs for parameters of this deep link.
     *
     * @see [addParameter]
     * @see [removeParameter]
     * @see [clearParameters]
     */
    val parameters: Map<String, String>
        get() = _parameters

    /**
     * Adds a new parameter to this deep link. If there is already a parameter with the same key,
     * the old one will be replaced.
     * E.g. `addParameter("vin", "123456789")`.
     */
    fun addParameter(key: String, value: String): DeepLink {
        _parameters[key] = value
        return this
    }

    /**
     * Adds all parameters from the given map. If a specific key already exists, the parameter
     * will be overwritten.
     */
    fun addAllParameters(parameters: Map<String, String>): DeepLink {
        _parameters.putAll(parameters)
        return this
    }

    /**
     * Removes the parameter with the given key if it exists.
     */
    fun removeParameter(key: String): DeepLink {
        _parameters.remove(key)
        return this
    }

    /**
     * Removes all parameters.
     */
    fun clearParameters(): DeepLink {
        _parameters.clear()
        return this
    }

    fun toUri() =
        Uri.Builder().apply {
            scheme(scheme)
            host?.let { authority(host) }
            parameters.forEach { appendQueryParameter(it.key, it.value) }
        }.build()
}