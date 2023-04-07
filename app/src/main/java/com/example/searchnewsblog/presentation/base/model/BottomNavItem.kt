package com.example.searchnewsblog.presentation.base.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * lsh 2023.04.02
 */
sealed class BottomNavItem(val route: String, val icon: ImageVector) {
    object Home : BottomNavItem("Home", Icons.Filled.Home)
    object Bookmark : BottomNavItem("Bookmark", Icons.Filled.Favorite)
    object Search : BottomNavItem("Search", Icons.Filled.Search)
    object Details : BottomNavItem("Details", Icons.Filled.Search)
}