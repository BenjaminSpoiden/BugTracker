package com.ben.bugtrackerclient.network

import android.util.Log
import com.ben.bugtrackerclient.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.math.log

object BugTrackerNetwork {

    val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    fun initializeRetrofit(accessToken: String? = null): BugTrackerService {
        Log.d("Tag", "accessToken= $accessToken")
        return Retrofit.Builder()
            .client(OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .build())
                }
                .also { client ->
                    if(BuildConfig.DEBUG) {
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        client.addInterceptor(logging)
                    }
                }
                .build())
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(BugTrackerService::class.java)
    }
}