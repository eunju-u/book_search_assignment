package com.example.domain.model

// 검색 결과 페이지
data class PageBooks(
    val books: List<BookModel>,
    val isEnd: Boolean
)