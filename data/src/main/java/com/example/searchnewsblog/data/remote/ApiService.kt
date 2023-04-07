package com.example.searchnewsblog.data.remote

import com.example.searchnewsblog.data.entity.ArticlePagesEntity
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("v2/everything")
    fun getEverything(@QueryMap query: Map<String, String>): Call<ArticlePagesEntity>

    @GET("v2/top-headlines")
    fun getTopHeadlines(@QueryMap query: Map<String, String>): Call<ArticlePagesEntity>
}
