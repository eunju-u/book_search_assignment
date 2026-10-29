package com.example.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.BookModel
import com.example.domain.usecase.GetFavoritesUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val sort = MutableStateFlow(FavoriteSortType.ASCENDING)
    private val filter = MutableStateFlow(FavoriteFilterType.RESET)

    // DB의 즐겨찾기 리스트와 정렬, 필터, 검색어를 combine으로 가공
    val uiState: StateFlow<FavoriteUiState> =
        combine(getFavoritesUseCase(), query, sort, filter) { allList, query, sort, filter ->
            var filtered = allList

            // 가격 범위가 있는 필터만 적용
            val range = filter.range
            if (range != null) {
                filtered = filtered.filter { it.price in range }
            }

            val keyword = query.trim()
            if (keyword.isNotEmpty()) {
                filtered = filtered.filter { book ->
                    book.title.contains(keyword, ignoreCase = true) ||
                            book.authors.any { it.contains(keyword, ignoreCase = true) }
                }
            }

            val sorted = when (sort) {
                FavoriteSortType.ASCENDING -> filtered.sortedBy { it.title }
                FavoriteSortType.DESCENDING -> filtered.sortedByDescending { it.title }
            }

            FavoriteUiState(query = query, sort = sort, filter = filter, books = sorted)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FavoriteUiState()
        )

    // 즐겨찾기 검색어 설정
    fun search(query: String) {
        this.query.value = query
    }

    // 정렬 타입 선택
    fun selectSort(sort: FavoriteSortType) {
        this.sort.value = sort
    }

    // 필터 타입 선택
    fun selectFilter(filter: FavoriteFilterType) {
        this.filter.value = filter
    }

    // 즐겨찾기 토글
    fun toggleFavorite(book: BookModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase(book)
        }
    }
}