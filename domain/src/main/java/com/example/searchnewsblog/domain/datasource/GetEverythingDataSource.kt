package com.example.searchnewsblog.domain.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchnewsblog.domain.NewsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.single
import timber.log.Timber

class GetEverythingDataSource(
    private val name: String,
    private val sort: NewsUseCase.Sort,
    private val useCase: NewsUseCase,
) : PagingSource<Int, Any>() {

    //======================================================================
    // Override Methods
    //======================================================================

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        val position = params.key ?: 1
        return loadRemoteResult(position, params)
    }

    //======================================================================
    // Private Methods
    //======================================================================

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun loadRemoteResult(
        position: Int,
        params: LoadParams<Int>,
    ): LoadResult<Int, Any> {
        return useCase.getArticleList(page = position, query = name, sort = sort)
            .mapLatest { result ->
                val items = result?.articles ?: emptyList()
                val nextKey = nextKey(position, items)
                val preKey = prevKey(position)

                val offset = "##".repeat(5)
                Timber.d("${offset}load.remote${offset}")
                Timber.d("position(${params.key}): ${position}, itemSize : ${items.size}")
                Timber.d("nextKey: ${nextKey}")
                Timber.d("preKey: ${preKey}")
                val out: LoadResult<Int, Any> =
                    LoadResult.Page(data = items, prevKey = preKey, nextKey = nextKey)
                out
            }.catch { e ->
                Timber.d("error > $e")
                val error = LoadResult.Error<Int, Any>(e)
                emit(error)
            }.single()
    }

    private fun prevKey(position: Int): Int? {
        val key = position - 1
        return if (key <= 0) {
            null
        } else {
            key
        }
    }

    private fun nextKey(position: Int, items: List<Any>?): Int? {
        if (items == null) {
            return null
        }
        val key = if (items.isEmpty()) {
            null
        } else {
            position + 1
        }
        /* 현재 페이지와 요청할 페이지 파라미터가 같을경우 에러 발생됨 */
        return if (position == key) {
            null
        } else {
            key
        }
    }
}