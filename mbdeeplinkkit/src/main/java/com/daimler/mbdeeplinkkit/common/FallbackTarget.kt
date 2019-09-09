package com.daimler.mbdeeplinkkit.common

/**
 * The fallback target for an action that should open a deep link.
 * This fallback target is used as an alternative if the specified deep link cannot be opened.
 */
enum class FallbackTarget {

    /** No fallback target. */
    NONE,

    /** Redirect to the store link of the app of the specified deep link. */
    STORE,

    /** Redirect to the marketing link of the app of the specified deep link. */
    MARKETING
}