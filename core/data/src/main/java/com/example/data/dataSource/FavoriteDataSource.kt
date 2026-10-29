package com.example.data.dataSource

import com.example.database.FavoriteDao
import com.example.database.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// 즐겨찾기 데이터 Room DB 조회, 저장
class FavoriteDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    suspend fun toggleFavorite(book: FavoriteEntity) = favoriteDao.toggleFavorite(book)

    // 즐겨찾기 추가
    suspend fun addFavorite(entity: FavoriteEntity) = favoriteDao.insert(entity)

    // 즐겨찾기 삭제
    suspend fun removeFavorite(bookId: String) = favoriteDao.deleteById(bookId)

    // 즐겨찾기 전체 목록
    fun getAllFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavoriteFlow()

    // 즐겨찾기한 도서 id 목록
    fun getAllIdsFlow(): Flow<List<String>> = favoriteDao.getAllIdsFlow()

    // 즐겨찾기 도서 하나 가져옴. 없으면 null 반환
    suspend fun getFavorite(bookId: String): FavoriteEntity? = favoriteDao.getById(bookId)
}