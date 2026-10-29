package com.example.domain.usecase

import com.example.domain.model.BookModel
import com.example.domain.repository.FavoriteRepository
import javax.inject.Inject

/// 즐겨찾기에 있는 도서 데이터 추가/삭제하는 seCase
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(book: BookModel) = repository.toggleFavorite(book)
}