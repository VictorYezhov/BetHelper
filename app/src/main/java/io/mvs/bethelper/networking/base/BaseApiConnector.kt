package io.mvs.bethelper.networking.base

import android.util.Log
import io.mvs.bethelper.BuildConfig

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseApiConnector <T> (client: Class<T>, private val baseUrl : String = BuildConfig.BASE_URL) {


    protected val apiClient : T

    init {
        val logInterceptor = HttpLoggingInterceptor { message -> Log.w("Retrofit", message) }
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(logInterceptor)
                    .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
                    .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                    .readTimeout(1, TimeUnit.MINUTES) // read timeout
                    .build())
            .baseUrl(baseUrl)
            .build()

        apiClient = retrofit.create(client)
    }

}