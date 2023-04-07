package com.example.searchnewsblog.domain.model

/**
 * lsh 2023.04.05
 */
data class KeywordArticleList(
    val keyword: String?,
    val itemList: List<Article>?,
)