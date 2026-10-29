package com.example.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.BookModel
import com.example.domain.usecase.GetFavoriteIdsUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

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
        viewModelScope.launch { toggleFavoriteUseCase(book) }
    }
}