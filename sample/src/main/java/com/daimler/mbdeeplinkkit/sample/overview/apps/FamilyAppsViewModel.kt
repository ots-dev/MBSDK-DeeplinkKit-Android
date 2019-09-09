package com.daimler.mbdeeplinkkit.sample.overview.apps

import androidx.lifecycle.ViewModel
import com.daimler.mbdeeplinkkit.MBDeepLinkKit
import com.daimler.mbdeeplinkkit.common.FamilyApp
import com.daimler.mbdeeplinkkit.sample.app.DeeplinkApplication
import com.daimler.mbloggerkit.MBLoggerKit
import com.daimler.mbuikit.components.recyclerview.MutableLiveArrayList
import com.daimler.mbuikit.lifecycle.events.MutableLiveEvent
import com.daimler.mbuikit.utils.extensions.mutableLiveDataOf

class FamilyAppsViewModel : ViewModel(), FamilyAppItem.FamilyAppEvents {

    val progressVisible = mutableLiveDataOf(false)
    val items = MutableLiveArrayList<FamilyAppItem>()

    val appSelectedEvent = MutableLiveEvent<String>()

    init {
        loadApps()
    }

    override fun onAppSelected(app: FamilyApp) {
        appSelectedEvent.sendEvent(app.appId)
    }

    private fun loadApps() {
        onLoading()
        MBDeepLinkKit.appService().fetchAppsAndDeepLinks(DeeplinkApplication.TOKEN)
            .onComplete {
                MBLoggerKit.d("Loaded ${it.size} apps.")
                mapAndShowItems(it)
            }.onFailure {
                MBLoggerKit.e("Failed to load apps.")
            }.onAlways { _, _, _ ->
                onLoadingFinished()
            }
    }

    private fun mapAndShowItems(items: List<FamilyApp>) {
        this.items.apply {
            value.clear()
            value.addAll(items.map { FamilyAppItem(it, this@FamilyAppsViewModel) })
            dispatchChange()
        }
    }

    private fun onLoading() {
        progressVisible.postValue(true)
    }

    private fun onLoadingFinished() {
        progressVisible.postValue(false)
    }
}