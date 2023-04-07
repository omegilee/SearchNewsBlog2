package  com.example.searchnewsblog.data.database

import android.content.Context
import com.example.searchnewsblog.data.database.dao.BookmarkDao
import com.example.searchnewsblog.data.database.dao.SearchKeywordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.createAppDatabase(context, "app.db")
    }

    @Singleton
    @Provides
    fun provideLocationDao(appDatabase: AppDatabase): SearchKeywordDao =
        appDatabase.searchKeywordDao()

    @Singleton
    @Provides
    fun provideBookmarkDao(appDatabase: AppDatabase): BookmarkDao =
        appDatabase.bookmarkDao()
}