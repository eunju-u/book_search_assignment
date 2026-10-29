package com.example.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.component.PopupWidget
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

    FavoriteScreen(
        uiState = uiState,
        onSearch = viewModel::search,
        onSortSelected = viewModel::selectSort,
        onFilterSelected = viewModel::selectFilter,
        onFavoriteClick = viewModel::toggleFavorite,
        onBookClick = onBookClick,
    )
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = uiState.books) { book ->
                BookItemWidget(
                    book = book,
                    onHeartClick = { onFavoriteClick(book) },
                    onClick = { onBookClick(book) },
                )
            }
        }
    }

    when (popup) {
        FavoritePopup.SORT -> PopupWidget(
            items = FavoriteSortType.entries,
            selectedItem = uiState.sort,
            getText = { resources.getString(it.textRes) },
            onItemSelected = {
                onSortSelected(it)
                popup = null
            },
            onDismiss = { popup = null }
        )

        FavoritePopup.FILTER -> PopupWidget(
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
