package com.example.searchnewsblog.data.entity

/**
 * lsh 2023.04.05
 */
data class HomeListEntity(
    val keyword: KeywordArticleListEntity?,
    val headlineList: List<ArticleEntity>?,
)