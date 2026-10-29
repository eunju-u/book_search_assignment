package com.example.data.repository

import com.example.data.dataSource.BookDataSource
import com.example.data.mapper.toModel
import com.example.domain.model.PageBooks
import com.example.domain.repository.BookRepository
import javax.inject.Inject

// 도서 검색 API 호출하는 BookRepository 구현체
class BookRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource
) : BookRepository {

    override suspend fun loadBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): PageBooks {
        val response = bookDataSource.getBooks(query, sort, page, size)

        val books = response.documents.map { it.toModel() }
        val isEnd = response.meta.is_end

        return PageBooks(
            books = books,
            isEnd = isEnd
        )
    }
}