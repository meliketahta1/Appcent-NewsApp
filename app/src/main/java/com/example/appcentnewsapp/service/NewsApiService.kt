package com.example.appcentnewsapp.service

import com.example.appcentnewsapp.model.NewsResponse
import com.example.appcentnewsapp.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NewsApiService {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                    val request = requestBuilder.build()
                    val response = chain.proceed(request)
                    response
                }
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(NewsApi::class.java)
        }
    }

}