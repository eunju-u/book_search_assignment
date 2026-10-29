package com.example.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.BookModel
import com.example.domain.usecase.GetFavoriteIdsUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    // 즐겨찾기 토글 실패 여부
    private val _favoriteError = MutableStateFlow(false)
    val favoriteError = _favoriteError.asStateFlow()

    // 즐겨찾기 토글 에러 상태 초기화
    fun dismissFavoriteError() {
        _favoriteError.value = false
    }

    // DB 응답 전에는 리스트뷰에서 전달받은 하트 상태 사용
    private val _favoriteIds = MutableStateFlow<Set<String>?>(null)
    val favoriteIds = _favoriteIds.asStateFlow()

    init {
        viewModelScope.launch {
            // DB 즐겨찾기 바뀔 때 ID 목록을 갱신
            getFavoriteIdsUseCase().collect { ids ->
                _favoriteIds.value = ids
            }
        }
    }

    fun toggleFavorite(book: BookModel) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(book)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _favoriteError.value = true
            }
        }
    }
}