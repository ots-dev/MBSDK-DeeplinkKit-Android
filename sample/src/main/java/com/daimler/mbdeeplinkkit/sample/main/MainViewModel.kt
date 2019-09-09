package com.daimler.mbdeeplinkkit.sample.main

import androidx.lifecycle.ViewModel
import com.daimler.mbdeeplinkkit.MBDeepLinkKit
import com.daimler.mbdeeplinkkit.sample.app.DeeplinkApplication
import com.daimler.mbloggerkit.MBLoggerKit
import com.daimler.mbuikit.lifecycle.events.MutableLiveUnitEvent
import com.daimler.mbuikit.utils.extensions.mutableLiveDataOf

class MainViewModel : ViewModel() {

    val progressVisible = mutableLiveDataOf(false)

    val showOverviewEvent = MutableLiveUnitEvent()

    fun onFetchDeepLinksClicked() {
        onLoading()
        MBDeepLinkKit.appService().fetchAppsAndDeepLinks(DeeplinkApplication.TOKEN)
            .onComplete { apps ->
                MBLoggerKit.d("Fetched apps.")
                apps.forEach {
                    MBLoggerKit.d(it.toString())
                }
            }.onFailure {
                MBLoggerKit.e("Failed to fetch apps.")
            }.onAlways { _, _, _ ->
                onLoadingFinished()
            }
    }

    fun onLoadDeepLinksClicked() {
        MBDeepLinkKit.deepLinkProvider().getAllDeepLinks()?.forEach {
            MBLoggerKit.d("$it")
        } ?: MBLoggerKit.e("No deep links found.")
    }

    fun onClearCacheClicked() {
        MBDeepLinkKit.clearCache()
    }

    fun onShowOverviewClicked() {
        showOverviewEvent.sendEvent()
    }

    private fun onLoading() {
        progressVisible.postValue(true)
    }

    private fun onLoadingFinished() {
        progressVisible.postValue(false)
    }
}