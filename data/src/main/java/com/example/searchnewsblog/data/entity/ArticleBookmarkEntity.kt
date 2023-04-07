package com.example.searchnewsblog.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ArticleBookmarkEntity",
    indices = [Index(value = ["title", "publishedAt"], unique = true)]
)
data class ArticleBookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val bookmarkId: Long = 0,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    @Embedded
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val createAt: String = "",
    val updateAt: String = ""
) {
    @Entity(
        tableName = "Source"
    )
    data class Source(
        @PrimaryKey(autoGenerate = true)
        val sourceId: Long = 0,
        val id: String?,
        val name: String?
    )
}