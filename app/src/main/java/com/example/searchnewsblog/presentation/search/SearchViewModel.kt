package com.example.searchnewsblog.presentation.search

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * lsh 2023.04.02
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val useCase: NewsUseCase
) : ViewModel() {

    val pages
        get() = _pages

    val sort
        get() = _sort

    val searchText
        get() = _searchText

    private val _pages = MutableStateFlow<PagingData<Any>>(PagingData.empty())

    private val _searchText = MutableStateFlow("")

    private val _sort = MutableStateFlow<NewsUseCase.Sort>(NewsUseCase.Sort.PublishedAt)

    fun load(query: String, sort: NewsUseCase.Sort) {
        viewModelScope.launch {
            useCase.getArticlePaging(query, sort)
                .cachedIn(viewModelScope)
                .catch { e ->
                    Timber.e(e)
                }.collectLatest {
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