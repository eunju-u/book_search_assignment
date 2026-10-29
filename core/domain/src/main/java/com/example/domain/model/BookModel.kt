package com.example.domain.model

// 도서 정보
data class BookModel(
    val title: String, // 도서 제목
    val authors: List<String>, // 도서 저자 리스트
    val publisher: String, // 도서 출판사
    val datetime: String, // 도서 출판일
    val price: Int, // 도서 정가
    val thumbnail: String,  // 도서 표지 미리보기 URL
    val isFavorite: Boolean = false, // 즐겨찾기 여부
)