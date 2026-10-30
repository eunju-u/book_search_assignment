package com.example.domain.repository

import com.example.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

// 즐겨찾기 추가,삭제 및 조회 기능 인터페이스
interface FavoriteRepository {
    // 즐겨찾기 토글
    suspend fun toggleFavorite(book: BookModel)

    // 즐겨찾기 지정한 상태 설정
    suspend fun setFavorite(book: BookModel, isFavorite: Boolean)

    // 즐겨찾기 전체 목록
    fun getAllFavorites(): Flow<List<BookModel>>

    // 즐겨찾기한 도서 id 목록
    fun getFavoriteIdsFlow(): Flow<List<String>>
}