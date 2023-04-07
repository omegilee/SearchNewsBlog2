package com.example.searchnewsblog.presentation.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.searchnewsblog.domain.NewsUseCase
import com.example.searchnewsblog.domain.model.Article
import com.example.searchnewsblog.presentation.base.*
import kotlinx.coroutines.launch

/**
 * lsh 2023.04.02
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel(), navController: NavController) {
    val lazyState = rememberLazyListState()
    val textState = viewModel.searchText.collectAsState(initial = viewModel.searchText.value)
    val sort = viewModel.sort.collectAsState(NewsUseCase.Sort.PublishedAt)
    val pageItems = viewModel.pages.collectAsLazyPagingItems()

    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {
        SearchView(textState.value, onReset = {
            viewModel.searchText.value = ""
        }, onDone = {
            scope.launch {
                lazyState.scrollToItem(index = 0)
            }
            viewModel.searchText.value = it
            viewModel.load(it, sort.value)
        }, onSort = { sort ->
            scope.launch {
                lazyState.scrollToItem(index = 0)
            }
            viewModel.sort.value = sort
            viewModel.load(textState.value, sort)
        })
        if (textState.value.isEmpty()) {
            EmptyRender("검색 결과가 없습니다.")
        } else {
            LazyColumn(
                state = lazyState
            ) {
                itemsIndexed(pageItems) { index, value ->
                    if (value is Article) {
                        ItemArticle(value, onBookmark = { checked ->
                            value.isBookmark = checked
                            viewModel.updateArticle(value)
                        }, modifier = Modifier.clickable {
                            navController.goDetails(value)
                        })
                    }
                    pageItems.ObserveStatus(emptyRender = {
                        EmptyRender("검색 결과가 없습니다.")
                    }) {
                        LoadingRender()
                    }
                }
            }
        }
    }
}

@Composable
fun SortSelectDialog(onDone: (value: NewsUseCase.Sort) -> Unit) {
    val items = arrayListOf(
        NewsUseCase.Sort.Popularity,
        NewsUseCase.Sort.PublishedAt,
        NewsUseCase.Sort.Relevancy
    )
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        Dialog(onDismissRequest = {
        }) {
            Surface(
                modifier = Modifier
                    .width(200.dp)
                    .wrapContentHeight()
            ) {
                LazyColumn {
                    itemsIndexed(items) { index, item ->
                        Text(
                            text = item.type,
                            modifier = Modifier
                                .padding(14.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onDone(item)
                                    showDialog = false
                                })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView(
    text: String,
    onReset: () -> Unit,
    onDone: (value: String) -> Unit,
    onSort: (sort: NewsUseCase.Sort) -> Unit
) {
    val current = remember {
        mutableStateOf(text)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    var dialogOpen by remember {
        mutableStateOf(false)
    }
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = current.value,
            onValueChange = { value ->
                current.value = value
            },
            modifier = Modifier.weight(1f, fill = true),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 18.sp
            ),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (current.value.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            current.value = ""
                            onReset()
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                onDone(current.value)
            }),

            singleLine = true,
            shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                cursorColor = Color.White,
                leadingIconColor = Color.White,
                trailingIconColor = Color.White,
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Box(
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    dialogOpen = true
                }, Alignment.Center
        ) {
            Text(
                text = "정렬", textAlign = TextAlign.Center,
                style = TextStyle(color = Color.White)
            )
        }


    }
    if (dialogOpen) {
        SortSelectDialog(onDone = {
            dialogOpen = false
            onSort(it)
        })
    }
}
