package com.example.searchnewsblog.data.repository

import com.example.searchnewsblog.data.database.dao.SearchKeywordDao
import com.example.searchnewsblog.data.entity.ArticleEntity
import com.example.searchnewsblog.data.entity.HomeListEntity
import com.example.searchnewsblog.data.entity.KeywordArticleListEntity
import com.example.searchnewsblog.data.repository.base.Repository
import com.example.searchnewsblog.data.repository.datasource.GetEverythingDatasource
import com.example.searchnewsblog.data.repository.datasource.GetTopHeadlinesDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class HomeListRepository @Inject constructor(
    private val searchKeywordDao: SearchKeywordDao,
    private val getEverythingDatasource: GetEverythingDatasource,
    private val getTopHeadlinesDatasource: GetTopHeadlinesDatasource,
) : Repository<HomeListRepository.Parameter, HomeListEntity?>() {

    override fun execute(): Flow<HomeListEntity?> {
        return combine(
            recentKeywordArticleList(),
            headlineArticleList()
        ) { recentKeywordArticleList, headlineArticleList ->
            logD("-------------execute >> call-------------")
            logD("recentKeywordArticleList : ${recentKeywordArticleList}")
            logD("headlineArticleList : ${headlineArticleList != null}")
            HomeListEntity(recentKeywordArticleList, headlineArticleList)
        }.catch { e ->
            logE("error1 > $e")
        }.flowOn(Dispatchers.IO)
    }

    private fun recentKeywordArticleList(): Flow<KeywordArticleListEntity?> {
        return flow {
            logD("recentKeywordArticleList start")
            emit(searchKeywordDao.recentKeyword()?.firstOrNull()?.name)
        }.map { it ->
            val keyword = it.orEmpty()
            val articleList = if (keyword.isEmpty().not()) {
                getEverythingDatasource.execute {
                    this.page = 1
                    this.q = keyword
                }.map {
                    it?.articles
                }.catch { e ->
                    emit(null)
                }.singleOrNull()
            } else {
                null
            }
            if (keyword.isNotEmpty() && articleList != null && articleList.isEmpty().not()) {
                KeywordArticleListEntity(keyword, articleList)
            } else {
                null
            }
        }.catch { e ->
            logE("recentKeywordArticleList error > $e")
            emit(null)
        }
    }

    private fun headlineArticleList(): Flow<List<ArticleEntity>?> {
        return getTopHeadlinesDatasource.execute {
        }.map {
            it?.articles
        }.catch { e ->
            logE("headlineArticleList error > $e")
            emit(null)
        }
    }

    private fun logD(message: String) {
        Timber.d("[${Thread.currentThread().name}]" + message)
    }

    private fun logE(message: String) {
        Timber.e("[${Thread.currentThread().name}]" + message)
    }

    //======================================================================
    // Parameter
    //======================================================================

    class Parameter
}