package com.daimler.mbdeeplinkkit.common

/**
 * Models the result of an operation that tried to open a deep link.
 */
enum class DeepLinkOpenResult {

    /** The deep link could be opened. */
    SUCCESS,

    /** The deep link was available but no receiver app is installed and the fallback was opened instead. */
    FALLBACK_OPENED,

    /** The deep link could not be found in the database or is not eligible for the vehicle. */
    NOT_AVAILABLE,

    /** The deep link was available but there is no receiver app installed on the device and no fallback could be opened. */
    NO_TARGET_FOUND
}