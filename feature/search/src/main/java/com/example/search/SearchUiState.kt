package com.example.search

// 검색 화면 상태
data class SearchUiState(
    val query: String = "",
    val sort: SearchSortType = SearchSortType.ACCURACY,
)