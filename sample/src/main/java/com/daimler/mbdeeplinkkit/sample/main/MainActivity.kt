package com.daimler.mbdeeplinkkit.sample.main

import androidx.databinding.ViewDataBinding
import com.daimler.mbdeeplinkkit.sample.BR
import com.daimler.mbdeeplinkkit.sample.R
import com.daimler.mbdeeplinkkit.sample.overview.apps.FamilyAppsActivity
import com.daimler.mbdeeplinkkit.sample.utils.extensions.newViewModel
import com.daimler.mbuikit.components.activities.MBBaseViewModelActivity
import com.daimler.mbuikit.lifecycle.events.LiveEventObserver

class MainActivity : MBBaseViewModelActivity<MainViewModel>() {

    override fun createViewModel(): MainViewModel = newViewModel()

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun getModelId(): Int = BR.model

    override fun onBindingCreated(binding: ViewDataBinding) {
        super.onBindingCreated(binding)

        viewModel.showOverviewEvent.observe(this, onShowOverviewEvent())
    }

    private fun onShowOverviewEvent() = LiveEventObserver<Unit> {
        startActivity(FamilyAppsActivity.getStartIntent(this))
    }
}