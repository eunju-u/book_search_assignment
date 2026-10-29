package com.example.search.model

import com.example.search.SearchSortType

// 같은 검색어와 정렬로 다시 검색해도 새 요청으로 구분한다.
internal data class SearchRequest(
    val query: String,
    val sort: SearchSortType,
    val requestId: Int,
)