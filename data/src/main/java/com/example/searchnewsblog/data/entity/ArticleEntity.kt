package com.example.searchnewsblog.data.entity

data class ArticleEntity(
        val author: String?,
        val content: String?,
        val description: String?,
        val publishedAt: String?,
        val source: Source?,
        val title: String?,
        val url: String?,
        val urlToImage: String?,
        val isBookmark: Boolean = false,
    ) {
        data class Source(
            val id: String?,
            val name: String?
        )
    }