package com.example.searchnewsblog.data.repository

import com.example.searchnewsblog.data.database.dao.BookmarkDao
import com.example.searchnewsblog.data.repository.base.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BookmarkToggleRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : Repository<BookmarkToggleRepository.Parameter, Long>() {

    //======================================================================
    // Override Methods
    //======================================================================

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<Long?> {
        return flow {
            emit(param?.articleEntityToJson)
        }.mapLatest {
            if (it.isNullOrEmpty()) {
                throw NullPointerException("null articleEntityToJson")
            }
            bookmarkDao.toggleBookmark(it)
        }.flowOn(Dispatchers.IO)
    }

    //======================================================================
    // Parameter
    //======================================================================

    class Parameter {

        var articleEntityToJson: String? = null
    }
}