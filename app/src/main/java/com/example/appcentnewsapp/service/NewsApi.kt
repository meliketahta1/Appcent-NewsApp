package com.example.appcentnewsapp.service

import com.example.appcentnewsapp.model.NewsResponse
import com.example.appcentnewsapp.util.Constants.Companion.API_KEY

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        key: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>


}