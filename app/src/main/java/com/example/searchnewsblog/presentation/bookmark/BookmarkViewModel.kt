package com.example.searchnewsblog.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.searchnewsblog.domain.NewsUseCase
import com.example.searchnewsblog.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * lsh 2023.04.02
 */
@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val useCase: NewsUseCase
) : ViewModel() {

    val pages
        get() = _pages

    private val _pages = MutableStateFlow<PagingData<Any>>(PagingData.empty())

    fun load() {
        viewModelScope.launch {
            useCase.getBookmarkPaging()
                .cachedIn(viewModelScope)
                .catch { e ->
                    Timber.e(e)
                }.collectLatest {
                    Timber.d("BookmarkItemList > collectLatest ${it}")
                    _pages.emit(it)
                }
        }
    }

    fun updateArticle(article: Article) {
        viewModelScope.launch {
            useCase.toggleBookmark(article).catch { e ->
                Timber.e(e)
            }.collectLatest {
                Timber.d("success : $it")
            }
        }
    }
}