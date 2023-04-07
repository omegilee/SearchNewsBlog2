package com.example.searchnewsblog.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchnewsblog.domain.NewsUseCase
import com.example.searchnewsblog.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * lsh 2023.04.02
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val useCase: NewsUseCase
) : ViewModel() {

    private val _details = MutableStateFlow<List<Any>>(emptyList())

    val details
        get() = _details.asStateFlow()

    fun load() {
        viewModelScope.launch {
            useCase.getHomeList().collectLatest {
                _details.value = it?.asList() ?: emptyList()
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