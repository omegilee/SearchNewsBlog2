package com.example.searchnewsblog.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.searchnewsblog.R
import com.example.searchnewsblog.domain.model.Article

/**
 * lsh 2023.04.05
 */

@Composable
fun ItemArticle(
    item: Article,
    modifier: Modifier = Modifier,
    onBookmark: (checked: Boolean) -> Unit
) {
    val checked by lazy {
        mutableStateOf(item.isBookmark)
    }
    Row(
        modifier
            .background(Color.DarkGray)
            .padding(10.dp)
    ) {
        LazyImage(
            model = item.urlToImage.orEmpty(),
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .size(60.dp)
                .background(color = Color.Gray)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = item.title.orEmpty(),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 3,
                    modifier = Modifier.weight(weight = 1f, fill = true)
                )
                IconButton(onClick = {
                    checked.value = !checked.value
                    onBookmark(checked.value)
                }
                ) {
                    Icon(
                        painter = if (checked.value) {
                            painterResource(id = R.drawable.baseline_bookmark_added_24)
                        } else {
                            painterResource(id = R.drawable.baseline_bookmark_border_24)
                        }, contentDescription = ""
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description.orEmpty(),
                style = MaterialTheme.typography.body1.copy(color = Color.LightGray),
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.author.orEmpty(),
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}


@Composable
fun EmptyRender(message: String = "정보가 없습니다.") {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message)
    }
}

@Composable
fun LoadingRender() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.Red,
            modifier = Modifier.size(40.dp)
        )
    }
}