package com.example.data.repository

import com.example.data.dataSource.FavoriteDataSource
import com.example.common.di.ApplicationScope
import com.example.data.mapper.toEntity
import com.example.data.mapper.toModel
import com.example.domain.model.BookModel
import com.example.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.async
import javax.inject.Inject

// 즐겨찾기 FavoriteRepository 구현체
class FavoriteRepositoryImpl @Inject constructor(
    private val localDataSource: FavoriteDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : FavoriteRepository {

    // 즐겨찾기 토글 기능
    // room에 도서 id 데이터 있을 경우 제거, 없을 경우 추가
    override suspend fun toggleFavorite(book: BookModel) {
        applicationScope.async {
            localDataSource.toggleFavorite(book.toEntity())
        }.await()
    }

    // 즐겨찾기 전체 목록
    override fun getAllFavorites(): Flow<List<BookModel>> {
        return localDataSource.getAllFavorites().map { list -> list.map { it.toModel() } }
    }

    // 즐겨찾기한 도서 id 목록
    override fun getFavoriteIdsFlow(): Flow<List<String>> = localDataSource.getAllIdsFlow()
}