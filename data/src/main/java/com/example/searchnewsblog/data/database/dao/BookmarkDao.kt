package com.example.searchnewsblog.data.database.dao

import androidx.room.*
import com.example.searchnewsblog.data.database.TimestampConverter
import com.example.searchnewsblog.data.entity.ArticleBookmarkEntity
import com.example.searchnewsblog.data.entity.ArticleEntity
import com.example.searchnewsblog.data.entity.ArticlePagesEntity
import com.google.gson.Gson
import timber.log.Timber

/**
 * lsh 2023.04.03
 */
@Dao
interface BookmarkDao {

    @Query("select * from ArticleBookmarkEntity")
    fun all(): List<ArticleBookmarkEntity>

    @Query("select * from ArticleBookmarkEntity where title = :title and publishedAt = :publishedAt")
    fun findArticleBookmarkEntity(
        title: String,
        publishedAt: String
    ): ArticleBookmarkEntity?


    @Query("select * from ArticleBookmarkEntity order by createAt desc limit :start, :end")
    fun pages(start: Int, end: Int): List<ArticleBookmarkEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: ArticleBookmarkEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(value: ArticleBookmarkEntity)

    @Delete
    fun delete(value: ArticleBookmarkEntity)

    fun insertWithTimestamp(valueJson: String): Long {
        val time = TimestampConverter.fromTimestamp(System.currentTimeMillis())
        return valueJson.run {
            Sample.gson.fromJson(this, ArticleBookmarkEntity::class.java)
        }.copy(createAt = time, updateAt = time).run {
            insert(this)
        }
    }

    fun updateWithTimestamp(value: ArticleBookmarkEntity): Long {
        val time = TimestampConverter.fromTimestamp(System.currentTimeMillis())
        update(value.copy(updateAt = time))
        return value.bookmarkId
    }

    fun toggleBookmark(valueJson: String): Long {
        val article = valueJson.run {
            Sample.gson.fromJson(this, ArticleBookmarkEntity::class.java)
        }
        val find =
            findArticleBookmarkEntity(
                article?.title.orEmpty(),
                article?.publishedAt.orEmpty()
            )
        return if (find != null) {
            delete(find)
            find.bookmarkId
        } else {
            insertWithTimestamp(valueJson)
        }.also {
            Timber.d("result : $it")
        }
    }
}

object Sample {

    val gson = Gson()

    fun ArticleEntity.toJson(): String? {
        return gson.toJson(this)
    }

    fun create(): ArticleBookmarkEntity? {
        val v = "{\n" +
                "            \"source\": {\n" +
                "                \"id\": null,\n" +
                "                \"name\": \"Boing Boing\"\n" +
                "            },\n" +
                "            \"author\": \"Jason Weisberger\",\n" +
                "            \"title\": \"Musk's anti-union tweet judged unlawful, must rehire fired organizer\",\n" +
                "            \"description\": \"The 5th U.S. Circuit Court of Appeals has upheld the National Labor Relations Board's March 2021 order that Elon Musk must delete an anti-union tweet threatening to eliminate stock options for unionized workers at Tesla. Tesla will also have to hire back and …\",\n" +
                "            \"url\": \"https://boingboing.net/2023/04/01/musks-anti-union-tweet-judged-unlawful-must-rehire-fired-organizer.html\",\n" +
                "            \"urlToImage\": \"https://i0.wp.com/boingboing.net/wp-content/uploads/2022/12/elon2.jpg?fit=1200%2C780&ssl=1\",\n" +
                "            \"publishedAt\": \"2023-04-01T14:35:50Z\",\n" +
                "            \"content\": \"The 5th U.S. Circuit Court of Appeals has upheld the National Labor Relations Board's March 2021 order that Elon Musk must delete an anti-union tweet threatening to eliminate stock options for unioni… [+1364 chars]\"\n" +
                "        }"

        return Gson().fromJson(v, ArticleBookmarkEntity::class.java)
    }
}