package com.example.searchnewsblog.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.searchnewsblog.domain.model.Article
import com.example.searchnewsblog.presentation.base.model.BottomNavItem
import com.example.searchnewsblog.presentation.bookmark.BookmarkScreen
import com.example.searchnewsblog.presentation.details.DetailsScreen
import com.example.searchnewsblog.presentation.home.HomeScreen
import com.example.searchnewsblog.presentation.search.SearchScreen
import com.example.searchnewsblog.support.GsonUtil
import com.example.searchnewsblog.ui.theme.SearchNewsBlogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bottomBarState = remember { (mutableStateOf(true)) }
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val lifeCycleOwner = LocalLifecycleOwner.current
            val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
                "NaviHost requires a viewModelStoreOwner"
            }

            navController.setLifecycleOwner(lifeCycleOwner)
            navController.setViewModelStore(viewModelStoreOwner.viewModelStore)

            when (navBackStackEntry?.destination?.route) {
                BottomNavItem.Details.route -> {
                    bottomBarState.value = false
                }
                else -> {
                    bottomBarState.value = true
                }
            }

            SearchNewsBlogTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.background),
                    bottomBar = {
                        BottomNavigationBar(navController = navController, bottomBarState)
                    }
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        NavigationGraph(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, bottomBarState: MutableState<Boolean>) {
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Bookmark
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    if (bottomBarState.value.not()) {
        return
    }
    BottomNavigation {
        navItems.forEach { navItem ->
            BottomNavigationItem(
                icon = { Icon(navItem.icon, contentDescription = null) },
                label = { Text(navItem.route) },
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(BottomNavItem.Bookmark.route) {
            BookmarkScreen(navController = navController)
        }
        composable(BottomNavItem.Details.route) { e ->
            val article = e.arguments?.getString("articleToJson")?.run {
                GsonUtil.fromJson(this, Article::class.java)
            }
            DetailsScreen(navController = navController, article = article)
        }
    }
}