package com.example.favorite

import com.example.domain.model.BookModel

// 즐겨찾기 화면 ui 상태값
data class FavoriteUiState(
    val query: String = "",
    val sort: FavoriteSortType = FavoriteSortType.ASCENDING,
    val filter: FavoriteFilterType = FavoriteFilterType.RESET,
    val books: List<BookModel> = emptyList(),
)