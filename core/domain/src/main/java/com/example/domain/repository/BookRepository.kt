package com.example.domain.repository

import com.example.domain.model.PageBooks

// 도서 검색  Repository 인터페이스
interface BookRepository {
    // 페이지의 검색 결과 반환
    suspend fun loadBooks(query: String, sort: String, page: Int, size: Int): PageBooks
}
