package com.example.searchnewsblog.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.searchnewsblog.domain.model.Article
import com.example.searchnewsblog.domain.model.KeywordArticleList
import com.example.searchnewsblog.presentation.base.DisposableLifecycleEvent
import com.example.searchnewsblog.presentation.base.ItemArticle
import com.example.searchnewsblog.presentation.base.goDetails

/**
 * lsh 2023.04.02
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavController) {
    val lazyState = rememberLazyListState()
    val pageItems = viewModel.details.collectAsState(initial = emptyList())

    DisposableLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.load()
        }
    }

    if (pageItems.value.isEmpty()) {
        EmptyRender()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyState) {
            val items = pageItems.value
            itemsIndexed(items) { index, item ->
                if (item is KeywordArticleList) {
                    KeywordArticleList(item, viewModel, navController)
                } else if (item is String) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(10.dp)
                    )
                } else if (item is Article) {
                    ItemArticle(item = item, onBookmark = {
                        item.isBookmark = it
                        viewModel.updateArticle(item)
                    }, modifier = Modifier.clickable {
                        navController.goDetails(item)
                    })
                }
            }
        }
    }
}

@Composable
private fun KeywordArticleList(
    value: KeywordArticleList,
    viewModel: HomeViewModel,
    navController: NavController
) {
    val items = value.itemList ?: emptyList()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = "최근 검색어 ${value.keyword.orEmpty()}", style = MaterialTheme.typography.subtitle1)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(items) { index: Int, item: Article ->
                ItemArticle(item = item, onBookmark = {
                    item.isBookmark = it
                    viewModel.updateArticle(item)
                }, modifier = Modifier
                    .width(320.dp)
                    .clickable {
                        navController.goDetails(item)
                    })
            }
        }
    }
}

@Composable
private fun EmptyRender() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "홈정보가 없습니다.")
    }
}