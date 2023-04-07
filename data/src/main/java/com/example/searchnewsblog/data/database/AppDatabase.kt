package com.example.searchnewsblog.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.searchnewsblog.data.database.dao.BookmarkDao
import com.example.searchnewsblog.data.database.dao.SearchKeywordDao
import com.example.searchnewsblog.data.entity.ArticleBookmarkEntity
import com.example.searchnewsblog.data.entity.SearchKeywordEntity

/**
 * lsh 2023.04.03
 */
@Database(
    entities = [SearchKeywordEntity::class, ArticleBookmarkEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchKeywordDao(): SearchKeywordDao

    abstract fun bookmarkDao(): BookmarkDao

    companion object {

        fun createAppDatabase(
            context: Context,
            dbName: String
        ): AppDatabase {
            return Room
                .databaseBuilder(
                    context, AppDatabase::class.java,
                    dbName
                )
                .build()
        }
    }
}