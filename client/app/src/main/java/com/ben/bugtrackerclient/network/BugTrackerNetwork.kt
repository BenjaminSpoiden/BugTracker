package com.ben.bugtrackerclient.network

import com.ben.bugtrackerclient.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object BugTrackerNetwork {

    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    fun initializeRetrofit(): BugTrackerService {
        return Retrofit.Builder()
//            .client(OkHttpClient.Builder()
//                .addInterceptor { chain ->
//                    chain.proceed(chain.request()
//                        .newBuilder()
//                        .addHeader("Authorization", "")
//                        .build())
//                }
//                .also { client ->
//                    if(BuildConfig.DEBUG) {
//                        val logging = HttpLoggingInterceptor()
//                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//                        client.addInterceptor(logging)
//                    }
//                }
//                .build())
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(BugTrackerService::class.java)
    }
}