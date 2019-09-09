package com.daimler.mbdeeplinkkit.common

/** Represents an app from the app family. */
data class FamilyApp(

    /** The unique ID of this app. */
    val appId: String,

    /** The display name of this app. */
    val name: String,

    /** A short description about this app. */
    val description: String,

    /** The URI scheme of this app. */
    val scheme: String,

    /** An URL to the icon of this app. */
    val iconUrl: String?,

    /** The URL link to the play store version of this app. */
    val storeLink: String?,

    /** The URL link to the marketing version of this app. */
    val marketingLink: String?,

    /** A flag to determine the visibility of this app in the menu bar. */
    val showInMenuBar: Boolean,

    /** A list containing all [DeepLink]s available for this app. */
    val deepLinks: List<DeepLink>
)