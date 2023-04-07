package com.example.searchnewsblog.domain.model

data class ArticlePages(
    val articles: List<Article>?,
    val status: String?,
    val totalResults: Int?
)