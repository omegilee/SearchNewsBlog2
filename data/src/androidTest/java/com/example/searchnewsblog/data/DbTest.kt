package com.example.searchnewsblog.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.searchnewsblog.data.database.dao.BookmarkDao
import com.example.searchnewsblog.data.database.dao.SearchKeywordDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import timber.log.Timber.Forest.plant
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DbTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var searchKeywordDao: SearchKeywordDao

    @Inject
    lateinit var bookmarkDao: BookmarkDao

    @Before
    fun init() {
        hiltRule.inject()
        plant(Timber.DebugTree())
    }

    @Test
    fun searchKeywordInsertTest() {
        runBlocking {

            val keyword = arrayOf("사과", "배", "나무", "테슽라", "신한지주")

            repeat(20) {
                searchKeywordDao.insertWithTimestamp(keyword[it % keyword.size])
            }


            val all = searchKeywordDao.all()

            val success = (all?.size ?: 0) > 0


            all?.forEach { item ->
                Timber.d("item : $item")
            }



            Assert.assertEquals("result fail : $success", true, success)

        }
    }
}