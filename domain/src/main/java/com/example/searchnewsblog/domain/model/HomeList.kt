package com.example.searchnewsblog.domain.model

/**
 * lsh 2023.04.05
 */
data class HomeList(
    val keyword: KeywordArticleList?,
    val headlineList: List<Article>?,
) {

    fun asList(): MutableList<Any>? {
        val items = mutableListOf<Any>()
        if (keyword != null) {
            items.add(keyword)
        }

        headlineList?.run {
            items.add("헤드라인")
            items.addAll(this)
        }
        return if (items.isEmpty()) {
            null
        } else {
            items
        }
    }
}