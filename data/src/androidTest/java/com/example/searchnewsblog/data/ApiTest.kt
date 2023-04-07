package com.example.searchnewsblog.data

import com.example.searchnewsblog.data.repository.GetEverythingRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber
import timber.log.Timber.Forest.plant
import javax.inject.Inject


@HiltAndroidTest
class ApiTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: GetEverythingRepository

    @Before
    fun init() {
        hiltRule.inject()
        plant(Timber.DebugTree())
    }

    @Test
    fun getEverythingRepositoryTest() {
        runBlocking {
            val result = repository.parameter {
                q = "apple"
            }.execute().singleOrNull()
            Assert.assertEquals("result fail : $result", true, result != null)
        }
    }
}