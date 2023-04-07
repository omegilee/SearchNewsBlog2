package com.example.searchnewsblog.presentation.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LazyImage(
    model: String,
    modifier: Modifier,
) = GlideImage(model = model, contentDescription = "", modifier = modifier) {
    it.load(model).fitCenter()
}