package com.daimler.mbdeeplinkkit.sample.overview.apps

import com.daimler.mbdeeplinkkit.common.FamilyApp
import com.daimler.mbdeeplinkkit.sample.BR
import com.daimler.mbdeeplinkkit.sample.R
import com.daimler.mbuikit.components.recyclerview.MBBaseRecyclerItem

class FamilyAppItem(private val app: FamilyApp, private val callback: FamilyAppEvents) : MBBaseRecyclerItem() {

    val title = app.name
    val imageUrl = app.iconUrl

    override fun getLayoutRes(): Int = R.layout.item_family_app

    override fun getModelId(): Int = BR.item

    fun onItemClicked() {
        callback.onAppSelected(app)
    }

    interface FamilyAppEvents {
        fun onAppSelected(app: FamilyApp)
    }
}