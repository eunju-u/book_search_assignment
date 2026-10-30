package com.example.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import com.example.designsystem.component.CappedAdaptiveGridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import com.example.designsystem.component.ErrorHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.flow.combine
import com.example.domain.error.DataError
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ui.SelectionPopupWidget
import com.example.designsystem.component.PopupWidget
import com.example.designsystem.theme.BackgroundGray
import com.example.domain.model.BookModel
import com.example.ui.BookItemWidget
import com.example.ui.SortButton
import com.example.ui.TopWidget
import com.example.ui.errorMessageRes
import com.example.ui.R as UiR

// 검색 화면
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBookClick: (BookModel) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val books = viewModel.books.collectAsLazyPagingItems()
    val gridState = rememberLazyGridState()
    val resources = LocalResources.current
    val context = LocalContext.current
    val refresh = books.loadState.refresh
    val append = books.loadState.append
    val isNetworkRefreshError = (refresh as? LoadState.Error)?.error as? DataError.Network
    var isNetworkErrorDismissed by rememberSaveable(refresh) { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var isOnline by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(viewModel, books, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // 관찰을 시작할 때 현재 연결 상태를 다시 확인함
            isOnline = null
            combine(viewModel.networkUpdates, snapshotFlow { books.loadState }) { online, states ->
                online to states
            }.collect { (online, states) ->
                isOnline = online
                if (viewModel.shouldRetryAppend(online, states.refresh, states.append)) {
                    books.retry()
                }
            }
        }
    }

    // 새로고침 중인지 여부
    var isPullRefreshing by rememberSaveable { mutableStateOf(false) }
    // 팝업 노출 여부
    var showSort by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(refresh) {
        if (refresh !is LoadState.Loading) isPullRefreshing = false
    }

    LaunchedEffect(Unit) {
        viewModel.scrollToTop.collect {
            // 새 검색을 시작하면 당겨서 새로고침 상태는 끝냄
            isPullRefreshing = false
            gridState.scrollToItem(0)
        }
    }

    val favoriteError by viewModel.favoriteError.collectAsStateWithLifecycle()
    ErrorHost(
        hasError = favoriteError,
        message = stringResource(UiR.string.favorite_update_failed),
        onErrorShown = viewModel::dismissFavoriteError,
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopWidget(
                    title = stringResource(R.string.search),
                    onSearch = viewModel::search,
                    onBlankSearch = {
                        Toast.makeText(context, R.string.search_query_required, Toast.LENGTH_SHORT).show()
                    },
                    text = uiState.query,
                    selectedSort = stringResource(uiState.sort.textRes),
                    actions = {
                        SortButton(onClick = { showSort = true })
                    }
                )

                PullToRefreshBox(
                    isRefreshing = isPullRefreshing && refresh is LoadState.Loading,
                    onRefresh = {
                        // 검색어 없을 경우 새로고침 불가
                        if (uiState.query.isNotBlank()) {
                            isPullRefreshing = true
                            books.refresh()
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    when {
                        !isPullRefreshing &&
                            (refresh is LoadState.NotLoading || isNetworkRefreshError != null) &&
                            books.itemCount > 0 -> LazyVerticalGrid(
                            columns = CappedAdaptiveGridCells(minSize = 320.dp, maxColumns = 3),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            state = gridState,
                        ) {
                            items(count = books.itemCount) { index ->
                                val item = books[index] ?: return@items
                                BookItemWidget(
                                    book = item,
                                    onHeartClick = { viewModel.toggleFavorite(item) },
                                    onClick = { onBookClick(item) },
                                )
                            }

                            // 리스트 하단 안내
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                when {
                                    append is LoadState.Loading -> Box(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator(Modifier.size(32.dp))
                                    }

                                    // 다음 페이지 조회 실패 시 목록 이어서 다시 조회
                                    append is LoadState.Error -> Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(15.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = stringResource(
                                                if (isOnline == false && append.error is DataError.Network) {
                                                    R.string.waiting_for_network
                                                } else {
                                                    append.error.errorMessageRes()
                                                }
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                        if (!(isOnline == false && append.error is DataError.Network)) {
                                            Spacer(Modifier.height(5.dp))
                                            Button(onClick = { books.retry() }) {
                                                Text(text = stringResource(UiR.string.reset_load))
                                            }
                                        }
                                    }

                                    append.endOfPaginationReached -> Text(
                                        text = stringResource(
                                            R.string.search_results_end,
                                            SearchPagingSource.MAX_PAGE,
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(15.dp),
                                        textAlign = TextAlign.Center,
                                        color = Color.Gray
                                    )

                                    else -> {}
                                }
                            }
                        }

                        // 실패할 경우
                        refresh is LoadState.Error && isNetworkRefreshError == null ->
                            SearchStatusWidget(stringResource(refresh.error.errorMessageRes()))

                        // 검색 결과 없을 경우
                        refresh is LoadState.NotLoading && books.itemCount == 0 && uiState.query.isNotBlank() ->
                            SearchStatusWidget(stringResource(UiR.string.none_result))
                    }
                }
            }

            if (isNetworkRefreshError != null && !isNetworkErrorDismissed) {
                PopupWidget(
                    message = stringResource(UiR.string.connect_network),
                    confirmText = stringResource(R.string.confirm),
                    onDismiss = { isNetworkErrorDismissed = true },
                )
            }

            if (showSort) {
                SelectionPopupWidget(
                    items = SearchSortType.entries,
                    selectedItem = uiState.sort,
                    onItemSelected = {
                        viewModel.selectSort(it)
                        showSort = false
                    },
                    onDismiss = { showSort = false },
                    getText = { resources.getString(it.textRes) }
                )
            }

            // 최초 화면은 검색 전 상태에서는 refresh = Loading이라 계속 로딩 중으로 표시 됨. 검색했을 때만 로딩 표시
            val isSearched = uiState.query.isNotBlank()
            if (isSearched && refresh is LoadState.Loading && !isPullRefreshing) {
                // refresh indicator
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.Center)
                        // 하단 탭(56.dp)을 제외한 영역에서 그리므로 화면 전체 중앙으로 이동
                        .offset(y = 28.dp)
                )
            }
        }
    }
}

// 검색 결과 없거나, 실패 시 문구 노출 뷰
@Composable
private fun SearchStatusWidget(text: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}
