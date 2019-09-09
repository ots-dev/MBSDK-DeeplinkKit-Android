package com.daimler.mbdeeplinkkit.network

import android.content.Context
import android.net.ConnectivityManager
import com.daimler.mbdeeplinkkit.BuildConfig
import com.daimler.mbloggerkit.Priority
import com.daimler.mbnetworkkit.header.HeaderService
import com.daimler.mbnetworkkit.networking.ConnectivityInterceptor
import com.daimler.mbnetworkkit.networking.createHttpLoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object RetrofitServiceProvider {

    fun createDeepLinkApi(
        context: Context,
        baseUrl: String,
        headerService: HeaderService
    ): DeepLinkApi {
        return Retrofit.Builder().apply {
            client(
                OkHttpClient.Builder().apply {
                    addInterceptor(ConnectivityInterceptor(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager))
                    addInterceptor(headerService.createRisHeaderInterceptor())
                    addInterceptor(createHttpLoggingInterceptor(
                        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.HEADERS,
                        Priority.INFO
                    ))
                }.build()
            )
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(baseUrl)
        }.build().create(DeepLinkApi::class.java)
    }
}