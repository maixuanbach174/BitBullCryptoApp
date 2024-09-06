package com.bachphucngequy.bitbull.news.api

data class NewsModel(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)