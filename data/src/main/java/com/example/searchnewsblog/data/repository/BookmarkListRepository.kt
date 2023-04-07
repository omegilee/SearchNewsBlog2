package com.example.searchnewsblog.data.repository

import com.example.searchnewsblog.data.database.dao.BookmarkDao
import com.example.searchnewsblog.data.entity.ArticleBookmarkEntity
import com.example.searchnewsblog.data.repository.base.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class BookmarkListRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : Repository<BookmarkListRepository.Parameter, List<ArticleBookmarkEntity>?>() {

    private val pageLimit = 10

    //======================================================================
    // Override Methods
    //======================================================================

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<List<ArticleBookmarkEntity>?> {
        return flow {
            val end = param?.page.run {
                if (this != null) {
                    this * pageLimit
                } else {
                    pageLimit
                }
            }
            val start = (end - pageLimit).coerceAtLeast(0)
            logD("req start :$start, end : $end")

            emit(start to end)
        }.mapLatest {
            logD("before >> ${it}")
            val pages = bookmarkDao.pages(it.first, it.second)
            logD("after >> ${pages?.size}")
            pages

        }.flowOn(Dispatchers.IO)
    }

    private fun logD(message: String) {
        Timber.d("[${Thread.currentThread().name}]${message}")
    }

    //======================================================================
    // Parameter
    //======================================================================

    class Parameter {
        var page: Int = 1
    }
}