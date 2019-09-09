package com.daimler.mbdeeplinkkit.sample.overview.deeplinks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daimler.mbdeeplinkkit.MBDeepLinkKit
import com.daimler.mbdeeplinkkit.common.DeepLink
import com.daimler.mbuikit.components.recyclerview.MutableLiveArrayList
import com.daimler.mbuikit.lifecycle.events.MutableLiveEvent

class DeepLinkOverviewViewModel(appId: String?) : ViewModel(), DeepLinkOverviewContentItem.DeepLinkEvents {

    val items = MutableLiveArrayList<DeepLinkItem>()

    val deepLinkSelectedEvent = MutableLiveEvent<DeepLink>()

    init {
        loadDeepLinks(appId)
    }

    override fun onDeepLinkSelected(deepLink: DeepLink) {
        deepLinkSelectedEvent.sendEvent(deepLink)
    }

    private fun loadDeepLinks(appId: String?) {
        val links: List<DeepLink>? = appId?.let {
            MBDeepLinkKit.deepLinkProvider().getDeepLinksForApp(it)
        } ?: MBDeepLinkKit.deepLinkProvider().getAllDeepLinks()
        links?.sortedBy {
            it.scheme
        }?.let {
            mapAndShowDeepLinks(it)
        }
    }

    private fun mapAndShowDeepLinks(items: List<DeepLink>) {
        this.items.apply {
            var currentScheme = ""
            value.clear()
            items.forEach {
                if (it.scheme != currentScheme) {
                    currentScheme = it.scheme
                    value.add(DeepLinkOverviewTitleItem(it.scheme))
                }
                value.add(DeepLinkOverviewContentItem(it, this@DeepLinkOverviewViewModel))
            }
        }
    }

    class Factory(private val appId: String?) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DeepLinkOverviewViewModel(appId) as T
        }
    }
}