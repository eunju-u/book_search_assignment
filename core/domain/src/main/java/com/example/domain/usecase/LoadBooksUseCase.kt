package com.example.domain.usecase

import com.example.domain.model.PageBooks
import com.example.domain.repository.BookRepository
import javax.inject.Inject

// 도서 검색 UseCase
class LoadBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): PageBooks {
        return repository.loadBooks(query, sort, page, size)
    }
}