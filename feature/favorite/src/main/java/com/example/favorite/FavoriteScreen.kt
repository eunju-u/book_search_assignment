package com.example.favorite

import com.example.ui.R as UiR
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import com.example.designsystem.component.CappedAdaptiveGridCells
import androidx.compose.foundation.lazy.grid.items
import com.example.designsystem.component.ErrorHost
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.SelectionPopupWidget
import com.example.designsystem.theme.BackgroundGray
import com.example.domain.model.BookModel
import com.example.ui.BookItemWidget
import com.example.ui.FilterButton
import com.example.ui.SortButton
import com.example.ui.TopWidget

// 팝업 enum (정렬, 필터)
private enum class FavoritePopup { SORT, FILTER }

@Composable
fun FavoriteScreen(
    onBookClick: (BookModel) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 즐겨찾기 삭제 대기 상태 관찰
    val removal by viewModel.pendingRemoval.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val removedMessage = stringResource(R.string.favorite_removed)
    val undoLabel = stringResource(R.string.favorite_undo)
    LaunchedEffect(removal?.id) {
        val current = removal ?: return@LaunchedEffect

        // 삭제 완료 안내와 실행 취소 액션이 포함한 스낵바를 표시
        val result = snackbar.showSnackbar(
            removedMessage, actionLabel = undoLabel,
            withDismissAction = true, duration = SnackbarDuration.Long,
        )

        // 실행 취소를 선택하면 즐겨찾기 복구하고 그 외는 삭제 대기 상태 해제
        if (result == SnackbarResult.ActionPerformed) viewModel.undoRemoval(current)
        else viewModel.dismissRemoval(current.id)
    }

    val favoriteError by viewModel.favoriteError.collectAsStateWithLifecycle()
    ErrorHost(
        hasError = favoriteError,
        message = stringResource(UiR.string.favorite_update_failed),
        onErrorShown = viewModel::dismissFavoriteError,
    ) {
        Box(Modifier.fillMaxSize()) {
            FavoriteScreen(
                uiState = uiState,
                onSearch = viewModel::search,
                onSortSelected = viewModel::selectSort,
                onFilterSelected = viewModel::selectFilter,
                onFavoriteClick = viewModel::removeFavorite,
                onBookClick = onBookClick,
            )
            SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
        }
    }
}

// 즐겨찾기 화면
@Composable
internal fun FavoriteScreen(
    uiState: FavoriteUiState,
    onSearch: (String) -> Unit,
    onSortSelected: (FavoriteSortType) -> Unit,
    onFilterSelected: (FavoriteFilterType) -> Unit,
    onFavoriteClick: (BookModel) -> Unit,
    onBookClick: (BookModel) -> Unit,
) {
    val resources = LocalResources.current
    val context = LocalContext.current
    var popup by rememberSaveable { mutableStateOf<FavoritePopup?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopWidget(
            title = stringResource(R.string.favorite),
            onSearch = onSearch,
            text = uiState.query,
            selectedSort = stringResource(uiState.sort.textRes),
            actions = {
                FilterButton(
                    onClick = { popup = FavoritePopup.FILTER },
                    pointer = uiState.filter != FavoriteFilterType.RESET
                )
                SortButton(onClick = { popup = FavoritePopup.SORT })
            }
        )

        if (uiState.isLoaded && uiState.books.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(stringResource(
                    if (uiState.totalCount == 0) R.string.favorite_empty
                    else UiR.string.none_result
                ))
            }
        } else {
            LazyVerticalGrid(
                columns = CappedAdaptiveGridCells(minSize = 320.dp, maxColumns = 3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = uiState.books, key = { it.id }) { book ->
                    BookItemWidget(
                        book = book,
                        onHeartClick = { onFavoriteClick(book) },
                        onClick = { onBookClick(book) },
                    )
                }
            }
        }
    }

    when (popup) {
        FavoritePopup.SORT -> SelectionPopupWidget(
            items = FavoriteSortType.entries,
            selectedItem = uiState.sort,
            getText = { resources.getString(it.textRes) },
            onItemSelected = {
                onSortSelected(it)
                popup = null
            },
            onDismiss = { popup = null }
        )

        FavoritePopup.FILTER -> SelectionPopupWidget(
            items = FavoriteFilterType.entries,
            selectedItem = uiState.filter,
            getText = { resources.getString(it.textRes) },
            onItemSelected = {
                onFilterSelected(it)
                popup = null
            },
            onDismiss = { popup = null }
        )

        null -> {}
    }
}
