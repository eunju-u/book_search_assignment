package com.example.network.dto

// 카카오 도서 검색 응답 전체
data class BookResponse(
    val documents: List<BookDto>, // 응답 결과
    val meta: Meta // 응답 관련 정보
)
