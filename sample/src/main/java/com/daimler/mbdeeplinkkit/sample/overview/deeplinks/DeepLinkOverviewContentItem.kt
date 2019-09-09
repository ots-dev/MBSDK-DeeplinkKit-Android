package com.daimler.mbdeeplinkkit.sample.overview.deeplinks

import com.daimler.mbdeeplinkkit.common.DeepLink
import com.daimler.mbdeeplinkkit.sample.BR
import com.daimler.mbdeeplinkkit.sample.R

class DeepLinkOverviewContentItem(private val deepLink: DeepLink, private val callback: DeepLinkEvents) : DeepLinkItem() {

    val id = deepLink.id
    val iconUrl = deepLink.iconUrl
    val scheme = deepLink.scheme
    val host = deepLink.host ?: "-"
    val parameters = deepLink.parameters.joinToString() ?: "-"
    val vehicles = deepLink.vehicles.joinToString("\n") { it.finOrVin }.takeIf { it.isNotEmpty() }
        ?: "-"
    val fullLink = deepLink.toUri().toString()

    override fun getLayoutRes(): Int = R.layout.item_deep_link_overview_content

    override fun getModelId(): Int = BR.item

    fun onItemClicked() {
        callback.onDeepLinkSelected(deepLink)
    }

    private fun Map<String, String>.joinToString(): String? {
        return if (isEmpty()) {
            null
        } else {
            StringBuilder().apply {
                this@joinToString.forEach {
                    append("${it.key}: ${it.value}")
                    append('\n')
                }
            }.toString()
        }
    }

    interface DeepLinkEvents {
        fun onDeepLinkSelected(deepLink: DeepLink)
    }
}