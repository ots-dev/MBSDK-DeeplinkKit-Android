package com.daimler.mbdeeplinkkit.sample.overview.deeplinks

import android.content.Context
import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.daimler.mbdeeplinkkit.MBDeepLinkKit
import com.daimler.mbdeeplinkkit.common.DeepLink
import com.daimler.mbdeeplinkkit.common.FallbackTarget
import com.daimler.mbdeeplinkkit.sample.BR
import com.daimler.mbdeeplinkkit.sample.R
import com.daimler.mbdeeplinkkit.sample.databinding.ActivityDeeplinkOverviewBinding
import com.daimler.mbuikit.components.activities.MBBaseViewModelActivity
import com.daimler.mbuikit.lifecycle.events.LiveEventObserver
import com.daimler.mbuikit.utils.extensions.toast

class DeepLinkOverviewActivity : MBBaseViewModelActivity<DeepLinkOverviewViewModel>() {

    override fun createViewModel(): DeepLinkOverviewViewModel {
        val appId = intent.getStringExtra(ARG_APP_ID)
        val factory = DeepLinkOverviewViewModel.Factory(appId)
        return ViewModelProviders.of(this, factory).get(DeepLinkOverviewViewModel::class.java)
    }

    override fun getLayoutRes(): Int = R.layout.activity_deeplink_overview

    override fun getModelId(): Int = BR.model

    override fun onBindingCreated(binding: ViewDataBinding) {
        super.onBindingCreated(binding)

        (binding as ActivityDeeplinkOverviewBinding).apply {
            recyclerItems.addItemDecoration(DividerItemDecoration(this@DeepLinkOverviewActivity, DividerItemDecoration.VERTICAL))
        }

        viewModel.deepLinkSelectedEvent.observe(this, onDeepLinkSelected())
    }

    private fun onDeepLinkSelected() = LiveEventObserver<DeepLink> {
        val result = MBDeepLinkKit.openDeepLink(this, it, null, FallbackTarget.STORE)
        toast("Result = $result.")
    }

    companion object {

        private const val ARG_APP_ID = "arg.deep.link.overview.app.id"

        fun getStartIntent(context: Context, appId: String?): Intent {
            return Intent(context, DeepLinkOverviewActivity::class.java).apply {
                putExtra(ARG_APP_ID, appId)
            }
        }
    }
}