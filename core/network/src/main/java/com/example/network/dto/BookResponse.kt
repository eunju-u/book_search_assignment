package com.example.network.dto

data class BookResponse(
    val documents: List<BookDto>, // 응답 결과
    val meta: Meta // 응답 관련 정보
)
