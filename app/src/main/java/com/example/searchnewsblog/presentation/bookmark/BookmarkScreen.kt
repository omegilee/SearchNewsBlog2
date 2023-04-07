package com.example.searchnewsblog.presentation.bookmark

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.searchnewsblog.domain.model.Article
import com.example.searchnewsblog.presentation.base.*
import timber.log.Timber

/**
 * lsh 2023.04.02
 */
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BookmarkScreen(viewModel: BookmarkViewModel = hiltViewModel(), navController: NavController) {
    val lazyState = rememberLazyListState()
    val pageItems = viewModel.pages.collectAsLazyPagingItems()

    DisposableLifecycleEvent { _, event ->
        Timber.d("DisposableLifecycleEvent >> event : $event")
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.load()
        }
    }

    LazyColumn(
        state = lazyState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Timber.d("BookmarkItemList : ${pageItems.itemCount}")
        itemsIndexed(pageItems) { index, value ->
            if (value is Article) {
                ItemArticle(value, onBookmark = { checked ->
                    value.isBookmark = checked
                    viewModel.updateArticle(value)
                    pageItems.refresh()
                }, modifier = Modifier.clickable {
                    navController.goDetails(value)
                })
            }

            pageItems.ObserveStatus(emptyRender = {
                EmptyRender()
            }
            ) {
                LoadingRender()
            }
        }
    }
}

