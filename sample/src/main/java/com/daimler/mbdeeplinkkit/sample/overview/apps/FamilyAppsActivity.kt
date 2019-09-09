package com.daimler.mbdeeplinkkit.sample.overview.apps

import android.content.Context
import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.daimler.mbdeeplinkkit.MBDeepLinkKit
import com.daimler.mbdeeplinkkit.common.DeepLinkOpenResult
import com.daimler.mbdeeplinkkit.common.FallbackTarget
import com.daimler.mbdeeplinkkit.sample.BR
import com.daimler.mbdeeplinkkit.sample.R
import com.daimler.mbdeeplinkkit.sample.overview.deeplinks.DeepLinkOverviewActivity
import com.daimler.mbdeeplinkkit.sample.utils.extensions.newViewModel
import com.daimler.mbuikit.components.activities.MBBaseViewModelActivity
import com.daimler.mbuikit.components.dialogfragments.MBDialogFragment
import com.daimler.mbuikit.lifecycle.events.LiveEventObserver
import com.daimler.mbuikit.utils.extensions.toast

class FamilyAppsActivity : MBBaseViewModelActivity<FamilyAppsViewModel>() {

    override fun createViewModel(): FamilyAppsViewModel = newViewModel()

    override fun getLayoutRes(): Int = R.layout.activity_family_apps

    override fun getModelId(): Int = BR.model

    override fun onBindingCreated(binding: ViewDataBinding) {
        super.onBindingCreated(binding)

        viewModel.appSelectedEvent.observe(this, onAppSelected())
    }

    private fun onAppSelected() = LiveEventObserver<String> {
        MBDialogFragment.Builder(0).apply {
            withTitle(getString(R.string.dialog_app_title))
            withMessage(getString(R.string.dialog_app_msg))
            withPositiveButtonText(getString(R.string.dialog_app_open_app))
            withNegativeButtonText(getString(R.string.dialog_app_show_deeplink))
            withListener(object : MBDialogFragment.DialogListener {

                override fun onNegativeAction(id: Int) {
                    startActivity(DeepLinkOverviewActivity.getStartIntent(this@FamilyAppsActivity, it))
                }

                override fun onPositiveAction(id: Int) {
                    val result = MBDeepLinkKit.openFamilyApp(this@FamilyAppsActivity, it, FallbackTarget.STORE)
                    if (result != DeepLinkOpenResult.SUCCESS) {
                        toast("Could not open app: $result.")
                    }
                }
            })
        }.build().show(supportFragmentManager, null)
    }

    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, FamilyAppsActivity::class.java)
        }
    }
}