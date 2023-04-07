package com.example.searchnewsblog.presentation.base

import android.os.Parcelable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@Composable
fun <T> Flow<T>.collectAsStateWithLifecycleRemember(
    initial: T,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flowLifecycleAware = remember(this, lifecycleOwner) {
        flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
    }
    return flowLifecycleAware.collectAsState(initial)
}

@Composable
fun <T : Any> LazyPagingItems<T>.ObserveStatus(
    emptyRender: @Composable () -> Unit,
    loadingRender: @Composable () -> Unit,
): MutableState<Boolean?> {
    val loadState = this.loadState

    val state by lazy {
        mutableStateOf<Boolean?>(null)
    }

    @Composable
    fun EmptyProcess() {
        logW("MaterialTheme >> EmptyProcess, itemCount : $itemCount")
        if (itemCount <= 0) {
            emptyRender()
            state.value = false
        }
    }

    @Composable
    fun LoadingProcess() {
        logW("MaterialTheme >> LoadingProcess, itemCount : $itemCount")
        if (itemCount <= 0) {
            loadingRender()
            state.value = true
        }
    }

    when (loadState.refresh) {
        is LoadState.Loading -> {
            logW("MaterialTheme >> loadState.refresh is LoadState.Loading")
            LoadingProcess()
        }
        is LoadState.NotLoading -> {
            logW("MaterialTheme >> loadState.refresh is LoadState.NotLoading")
            EmptyProcess()
        }
        is LoadState.Error -> {
            logW("MaterialTheme >> loadState.refresh is LoadState.Error")
            EmptyProcess()
        }
        else -> {

        }
    }
    return state
}

@Composable
fun DisposableLifecycleEvent(eventObserver: LifecycleEventObserver) {
    with(LocalLifecycleOwner.current) {
        DisposableEffect(this) {
            with(lifecycle) {
                addObserver(eventObserver)
                onDispose {
                    removeObserver(eventObserver)
                }
            }
        }
    }
}

fun NavController.navigate(route: String, vararg args: Pair<String, String>) {
    navigate(route)
    requireNotNull(currentBackStackEntry?.arguments).apply {
        args.forEach { (key: String, arg: Any) ->
            putString(key, arg)
        }
    }
}

inline fun <reified T : Parcelable> NavBackStackEntry.requiredArg(key: String): T {
    return requireNotNull(arguments) { "arguments bundle is null" }.run {
        requireNotNull(getParcelable(key)) { "argument for $key is null" }
    }
}

private fun logW(message: String) {
    Timber.w(message)
}