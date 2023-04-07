package com.example.searchnewsblog.data.entity

data class ArticlePagesEntity(
    val articles: List<ArticleEntity>?,
    val status: String?,
    val totalResults: Int?
)