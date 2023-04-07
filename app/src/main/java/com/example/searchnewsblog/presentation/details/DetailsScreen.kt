package com.example.searchnewsblog.presentation.details

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.searchnewsblog.domain.model.Article
import com.example.searchnewsblog.presentation.home.DetailsViewModel

/**
 * lsh 2023.04.02
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailsScreen(
    article: Article?,
    viewModel: DetailsViewModel = hiltViewModel(),
    navController: NavController
) {
    val webViewClient = WebViewClient()

    val loadUrl by remember {
        mutableStateOf(article?.url.orEmpty())
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                elevation = 4.dp,
                title = {
                    Text(article?.title.orEmpty())
                },
                backgroundColor = MaterialTheme.colors.primarySurface,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                })
        },
        content = {
            AndroidView(factory = {
                WebView(it).apply {
                    this.webViewClient = webViewClient
                    this.loadUrl(loadUrl)
                }
            })
        })
}

