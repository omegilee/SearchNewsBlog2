package com.example.searchnewsblog.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.searchnewsblog.data.database.TimestampConverter
import com.example.searchnewsblog.data.entity.SearchKeywordEntity

/**
 * lsh 2023.04.03
 */
@Dao
interface SearchKeywordDao {

    @Query("select * from SearchKeywordEntity")
    fun all(): List<SearchKeywordEntity>?

    @Query("select * from SearchKeywordEntity order by createAt desc limit 10")
    fun recentKeyword(): List<SearchKeywordEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: SearchKeywordEntity): Long

    fun insertWithTimestamp(value: String): Long {
        return SearchKeywordEntity(
            name = value,
            createAt = TimestampConverter.fromTimestamp(System.currentTimeMillis()),
            updateAt = TimestampConverter.fromTimestamp(System.currentTimeMillis())
        ).run {
            insert(this)
        }
    }
}