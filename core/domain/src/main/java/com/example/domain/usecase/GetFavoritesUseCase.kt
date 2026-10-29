package com.example.domain.usecase

import com.example.domain.model.BookModel
import com.example.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// 즐겨찾기한 도서 전체 목록을 가져오는 UseCase
class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<BookModel>> = repository.getAllFavorites()
}