package com.daimler.mbdeeplinkkit.implementation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.daimler.mbdeeplinkkit.DeepLinkProvider
import com.daimler.mbdeeplinkkit.IntentService
import com.daimler.mbdeeplinkkit.common.*
import com.daimler.mbdeeplinkkit.persistence.FamilyAppsCache

internal class AndroidIntentService(
    private val appsCache: FamilyAppsCache,
    private val deepLinkProvider: DeepLinkProvider
) : IntentService {

    override fun openDeepLink(context: Context, deepLink: DeepLink, finOrVin: String?, fallbackTarget: FallbackTarget): DeepLinkOpenResult {
        val intent = deepLink.createIntent()
        return when {
            !finOrVin.isNullOrBlank() && !deepLink.isEligibleForVin(finOrVin) ->
                DeepLinkOpenResult.NOT_AVAILABLE
            intent.isResolvable(context) -> {
                context.startActivity(intent)
                DeepLinkOpenResult.SUCCESS
            }
            shouldOpenFallback(fallbackTarget) -> openFallbackTarget(context, deepLink, fallbackTarget)
            else -> DeepLinkOpenResult.NO_TARGET_FOUND
        }
    }

    override fun openDeepLink(context: Context, id: String, finOrVin: String?, fallbackTarget: FallbackTarget): DeepLinkOpenResult {
        return deepLinkProvider.getDeepLinkById(id, finOrVin)?.let {
            openDeepLink(context, it, finOrVin, fallbackTarget)
        } ?: DeepLinkOpenResult.NOT_AVAILABLE
    }

    override fun openFamilyApp(context: Context, app: FamilyApp, fallbackTarget: FallbackTarget): DeepLinkOpenResult {
        val intent = app.createIntent()
        return when {
            intent.isResolvable(context) -> {
                context.startActivity(intent)
                DeepLinkOpenResult.SUCCESS
            }
            shouldOpenFallback(fallbackTarget) -> openFallbackTarget(context, app, fallbackTarget)
            else -> DeepLinkOpenResult.NOT_AVAILABLE
        }
    }

    override fun openFamilyApp(context: Context, id: String, fallbackTarget: FallbackTarget): DeepLinkOpenResult {
        return appsCache.loadFamilyAppById(id)?.let {
            openFamilyApp(context, it, fallbackTarget)
        } ?: DeepLinkOpenResult.NOT_AVAILABLE
    }

    private fun openFallbackTarget(context: Context, deepLink: DeepLink, fallbackTarget: FallbackTarget): DeepLinkOpenResult {
        val app = appsCache.loadFamilyAppOfDeepLink(deepLink.id) ?: return DeepLinkOpenResult.NOT_AVAILABLE
        val intent = app.createFallbackIntent(fallbackTarget) ?: return DeepLinkOpenResult.NO_TARGET_FOUND
        return if (intent.isResolvable(context)) {
            context.startActivity(intent)
            DeepLinkOpenResult.FALLBACK_OPENED
        } else {
            DeepLinkOpenResult.NO_TARGET_FOUND
        }
    }

    private fun openFallbackTarget(context: Context, app: FamilyApp, fallbackTarget: FallbackTarget): DeepLinkOpenResult {
        val intent = app.createFallbackIntent(fallbackTarget) ?: return DeepLinkOpenResult.NO_TARGET_FOUND
        return if (intent.isResolvable(context)) {
            context.startActivity(intent)
            DeepLinkOpenResult.FALLBACK_OPENED
        } else {
            DeepLinkOpenResult.NO_TARGET_FOUND
        }
    }

    private fun FamilyApp.createIntent() =
        Intent().apply { data = Uri.parse(this@createIntent.scheme) }

    private fun DeepLink.createIntent() =
        Intent().apply { data = toUri() }

    private fun Intent.isResolvable(context: Context) =
        context.packageManager.resolveActivity(this, 0) != null

    private fun FamilyApp.createFallbackIntent(target: FallbackTarget): Intent? =
        getFallbackUrl(target)?.let {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(it)
            }
        }

    private fun shouldOpenFallback(target: FallbackTarget): Boolean =
        target != FallbackTarget.NONE

    private fun FamilyApp.getFallbackUrl(target: FallbackTarget): String? =
        when (target) {
            FallbackTarget.NONE -> null
            FallbackTarget.STORE -> storeLink
            FallbackTarget.MARKETING -> marketingLink
        }

    private fun DeepLink.isEligibleForVin(finOrVin: String) =
        vehicles.any { it.finOrVin == finOrVin }
}