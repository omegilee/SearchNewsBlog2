package com.example.searchnewsblog.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.searchnewsblog.data.repository.BookmarkListRepository
import com.example.searchnewsblog.data.repository.BookmarkToggleRepository
import com.example.searchnewsblog.data.repository.GetEverythingRepository
import com.example.searchnewsblog.data.repository.HomeListRepository
import com.example.searchnewsblog.domain.datasource.BookmarkDataSource
import com.example.searchnewsblog.domain.datasource.GetEverythingDataSource
import com.example.searchnewsblog.domain.model.Article
import com.example.searchnewsblog.domain.model.ArticlePages
import com.example.searchnewsblog.domain.model.HomeList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * lsh 2023.04.02
 */
class NewsUseCase @Inject constructor(
    private val repository: GetEverythingRepository,
    private val bookmarkToggleRepository: BookmarkToggleRepository,
    private val bookmarkListRepository: BookmarkListRepository,
    private val homeListRepository: HomeListRepository
) {

    private val gson by lazy {
        Gson()
    }

    fun getArticleList(
        query: String,
        page: Int = 1,
        sort: Sort = Sort.Popularity
    ): Flow<ArticlePages?> =
        repository.parameter {
            applySortBy(sort.type)
            this.q = query
            this.page = page
        }.execute().map {
            gson.toJson(it)
        }.map {
            gson.fromJson(it, ArticlePages::class.java)
        }

    fun getArticlePaging(query: String, ort: Sort = Sort.Popularity): Flow<PagingData<Any>> {
        return Pager(config = PagingConfig(
            enablePlaceholders = false,
            prefetchDistance = 5,
            initialLoadSize = 3,
            pageSize = 100
        ), pagingSourceFactory = {
            GetEverythingDataSource(query, ort, this)
        }).flow
    }

    fun toggleBookmark(value: Article): Flow<Long?> {
        return bookmarkToggleRepository.parameter {
            articleEntityToJson = gson.toJson(value)
        }.execute()
    }

    fun getBookmarkList(page: Int): Flow<List<Article>?> {
        return bookmarkListRepository.parameter {
            this.page = page
        }.execute().map {
            gson.toJson(it)
        }.map {
            val r: List<Article> = gson.fromJson(it, object : TypeToken<List<Article>>() {}.type)
            r
        }
    }

    fun getBookmarkPaging(): Flow<PagingData<Any>> {
        return Pager(config = PagingConfig(
            enablePlaceholders = false,
            prefetchDistance = 5,
            initialLoadSize = 1,
            pageSize = 10
        ), pagingSourceFactory = {
            BookmarkDataSource(this)
        }).flow
    }

    fun getHomeList(): Flow<HomeList?> {
        return homeListRepository.parameter {
        }.execute()
            .map {
                gson.toJson(it)
            }.map {
                gson.fromJson(it, HomeList::class.java)
            }
    }


    sealed class Sort(val type: String) {
        object Relevancy : Sort("relevancy")
        object PublishedAt : Sort("publishedAt")
        object Popularity : Sort("popularity")
    }
}