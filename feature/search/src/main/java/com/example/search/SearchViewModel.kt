package com.example.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.model.BookModel
import com.example.domain.usecase.GetFavoriteIdsUseCase
import com.example.domain.usecase.LoadBooksUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import com.example.search.model.SearchRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val loadBooksUseCase: LoadBooksUseCase,
    getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private companion object {
        const val PAGE_SIZE = 20 // 페이지당 20개
        const val PREFETCH_DISTANCE = 5

        // 스크롤 중 리스트의 마지막에서 5개 앞에 도달 시 다음 페이지 미리 요청
        val PAGING_CONFIG = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = false,
        )
    }

    // 검색어,정렬 상태
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // 검색 요청
    private val searchRequest = MutableStateFlow<SearchRequest?>(null)

    private val pagingBooks: Flow<PagingData<BookModel>> = searchRequest
        .filterNotNull()
        .flatMapLatest { request ->
            Pager(config = PAGING_CONFIG) {
                SearchPagingSource(loadBooksUseCase, request.query, request.sort)
            }.flow
        }
        .cachedIn(viewModelScope)

    // 검색 결과 - paging 결과, 즐겨찾기 정보 결합 후 즐겨찾기 여부 아이콘 노출
    val books: Flow<PagingData<BookModel>> =
        combine(pagingBooks, getFavoriteIdsUseCase()) { pagingData, favoriteIds ->
            pagingData.map { it.copy(isFavorite = it.id in favoriteIds) }
        }
            .cachedIn(viewModelScope)

    private val _scrollToTop = MutableSharedFlow<Unit>()
    val scrollToTop: SharedFlow<Unit> = _scrollToTop.asSharedFlow()

    // 검색어 입력 후 이동
    fun search(query: String) {
        _uiState.update { it.copy(query = query) }
        requestSearch()
    }

    // 정렬 선택
    fun selectSort(sort: SearchSortType) {
        _uiState.update { it.copy(sort = sort) }
        requestSearch()
    }

    // 즐겨찾기 토글 (추가/삭제)
    fun toggleFavorite(book: BookModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase(book)
        }
    }

    // 검색 요청
    private fun requestSearch() {
        val state = _uiState.value
        if (state.query.isBlank()) return

        searchRequest.update { previous ->
            SearchRequest(state.query, state.sort, requestId = (previous?.requestId ?: 0) + 1)
        }
        viewModelScope.launch { _scrollToTop.emit(Unit) }
    }
}