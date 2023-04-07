package com.example.searchnewsblog.data.repository

import com.example.searchnewsblog.data.database.dao.SearchKeywordDao
import com.example.searchnewsblog.data.entity.ArticlePagesEntity
import com.example.searchnewsblog.data.repository.base.Repository
import com.example.searchnewsblog.data.repository.datasource.GetEverythingDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEverythingRepository @Inject constructor(
    private val getEverythingDatasource: GetEverythingDatasource,
    private val searchKeywordDao: SearchKeywordDao
) : Repository<GetEverythingRepository.Parameter, ArticlePagesEntity>() {

    //======================================================================
    // Override Methods
    //======================================================================

    override fun execute(): Flow<ArticlePagesEntity?> {
        val query = param?.q
        return getEverythingDatasource.execute(param!!).onEach { response ->
            withContext(Dispatchers.IO) {
                launch {
                    if (query != null && query.isNotEmpty()) {
                        searchKeywordDao.insertWithTimestamp(query)
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    //======================================================================
    // Parameter
    //======================================================================

    class Parameter : GetEverythingDatasource.Parameter()
}