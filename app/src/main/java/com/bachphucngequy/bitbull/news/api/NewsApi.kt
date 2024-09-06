package com.bachphucngequy.bitbull.news.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("/v2/everything")
    suspend fun getNews(
        @Query("q") topic: String,
        @Query("page") pageNumber: Int = 1,
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ) : Response<NewsModel>
}