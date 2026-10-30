package com.example.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.BookModel
import com.example.domain.model.effectivePrice
import com.example.domain.usecase.GetFavoritesUseCase
import com.example.domain.usecase.SetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val setFavoriteUseCase: SetFavoriteUseCase,
) : ViewModel() {

    private var removalId = 0L
    private val _pendingRemoval = MutableStateFlow<FavoriteRemoval?>(null)
    val pendingRemoval = _pendingRemoval.asStateFlow()

    // 즐겨찾기 도서의 삭제 대기 상태를 해제
    fun dismissRemoval(id: Long) {
        if (_pendingRemoval.value?.id == id) _pendingRemoval.value = null
    }

    // 스낵바의 실행 취소 액션을 통해 도서 삭제를 취소하고 즐겨찾기를 복구함
    // 삭제 대기 상태를 해제 후 즐겨찾기 등록을 다시 요청
    fun undoRemoval(removal: FavoriteRemoval) {
        dismissRemoval(removal.id)
        viewModelScope.launch {
            try {
                setFavoriteUseCase(removal.book, true)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _favoriteError.value = true
            }
        }
    }

    // 즐겨찾기 토글 실패 여부
    private val _favoriteError = MutableStateFlow(false)
    val favoriteError = _favoriteError.asStateFlow()

    // 즐겨찾기 토글 에러 상태 초기화
    fun dismissFavoriteError() {
        _favoriteError.value = false
    }

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
                filtered = filtered.filter { it.effectivePrice in range }
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

            FavoriteUiState(query = query, sort = sort, filter = filter, books = sorted,
                totalCount = allList.size, isLoaded = true)
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

    // 삭제 성공 후 가장 최근 삭제 항목의 실행 취소를 제공한다.
    fun removeFavorite(book: BookModel) {
        viewModelScope.launch {
            try {
                setFavoriteUseCase(book, false)
                _pendingRemoval.value = FavoriteRemoval(++removalId, book)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _favoriteError.value = true
            }
        }
    }
}

data class FavoriteRemoval(val id: Long, val book: BookModel)
