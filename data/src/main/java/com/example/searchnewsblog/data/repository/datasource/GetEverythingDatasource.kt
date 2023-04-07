package com.example.searchnewsblog.data.repository.datasource

import com.example.searchnewsblog.data.BuildConfig
import com.example.searchnewsblog.data.database.dao.BookmarkDao
import com.example.searchnewsblog.data.entity.ArticlePagesEntity
import com.example.searchnewsblog.data.remote.ApiService
import com.example.searchnewsblog.data.repository.base.RemoteDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Call
import javax.inject.Inject

class GetEverythingDatasource @Inject constructor(
    private val service: ApiService,
    private val bookmarkDao: BookmarkDao
) : RemoteDataSource<GetEverythingDatasource.Parameter, ArticlePagesEntity, ArticlePagesEntity?>() {

    override fun fetchApi(param: Parameter?): Call<ArticlePagesEntity> {
        return service.getEverything(param?.toMap() ?: hashMapOf())
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

        private var sortBy: String? = "popularity"

        var q: String? = null

        var page: Int = 1

        @Throws(IllegalStateException::class)
        fun applySortBy(type: String) {
            val all = arrayOf("relevancy", "publishedAt", "popularity").all {
                it == type
            }
            check(all.not()) {
                "not match type : $type"
            }
            sortBy = type
        }

        fun toMap(): HashMap<String, String> {
            return hashMapOf(
                "q" to q.orEmpty(),
                "sortBy" to sortBy.orEmpty(),
                "page" to page.toString(),
                "apiKey" to BuildConfig.API_TOKEN
            )
        }
    }
}