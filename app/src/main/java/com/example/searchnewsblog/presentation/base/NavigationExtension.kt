package com.example.searchnewsblog.presentation.base

import androidx.navigation.NavController
import com.example.searchnewsblog.domain.model.Article
import com.example.searchnewsblog.presentation.base.model.BottomNavItem
import com.example.searchnewsblog.support.GsonUtil.toJson

/**
 * lsh 2023.04.07
 */

fun NavController.goDetails(item: Article) {
    this.navigate(
        BottomNavItem.Details.route, "articleToJson" to item.toJson().orEmpty()
    )
}