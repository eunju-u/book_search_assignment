package com.example.domain.usecase

import com.example.domain.model.BookModel
import com.example.domain.repository.FavoriteRepository
import javax.inject.Inject

// 즐겨찾기를 지정한 상태로 설정하는 UseCase
// true: 저장/복원, false: 삭제
class SetFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(book: BookModel, isFavorite: Boolean) =
        repository.setFavorite(book, isFavorite)
}