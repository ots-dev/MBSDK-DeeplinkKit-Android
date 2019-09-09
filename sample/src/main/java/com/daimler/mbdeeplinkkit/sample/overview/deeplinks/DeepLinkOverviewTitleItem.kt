package com.daimler.mbdeeplinkkit.sample.overview.deeplinks

import com.daimler.mbdeeplinkkit.sample.BR
import com.daimler.mbdeeplinkkit.sample.R

class DeepLinkOverviewTitleItem(val title: String) : DeepLinkItem() {

    override fun getLayoutRes(): Int = R.layout.item_deep_link_overview_title

    override fun getModelId(): Int = BR.item
}