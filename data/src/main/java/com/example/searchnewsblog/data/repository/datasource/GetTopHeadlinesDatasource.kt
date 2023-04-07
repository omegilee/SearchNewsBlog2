package com.example.searchnewsblog.data.repository.datasource

import com.example.searchnewsblog.data.BuildConfig
import com.example.searchnewsblog.data.database.dao.BookmarkDao
import com.example.searchnewsblog.data.entity.ArticlePagesEntity
import com.example.searchnewsblog.data.remote.ApiService
import com.example.searchnewsblog.data.repository.base.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Call
import javax.inject.Inject

class GetTopHeadlinesDatasource @Inject constructor(
    private val service: ApiService,
    private val bookmarkDao: BookmarkDao
) : RemoteDataSource<GetTopHeadlinesDatasource.Parameter, ArticlePagesEntity, ArticlePagesEntity?>() {

    override fun fetchApi(param: Parameter?): Call<ArticlePagesEntity> {
        return service.getTopHeadlines(param?.toMap() ?: hashMapOf())
    }

    override fun toObject(raw: ArticlePagesEntity?): ArticlePagesEntity? {
        return raw
    }

    override fun execute(param: Parameter): Flow<ArticlePagesEntity?> {
        return super.execute(param).map { response ->
            response?.copy(articles = response.articles?.map { item ->
                item.run {
                    copy(
                        isBookmark = bookmarkDao.findArticleBookmarkEntity(
                            title.orEmpty(),
                            publishedAt.orEmpty()
                        ) != null
                    )
                }
            })
        }.flowOn(Dispatchers.IO)
    }

    open class Parameter {
        fun toMap(): HashMap<String, String> {
            return hashMapOf(
                "country" to "kr",
                "apiKey" to BuildConfig.API_TOKEN
            )
        }
    }
}