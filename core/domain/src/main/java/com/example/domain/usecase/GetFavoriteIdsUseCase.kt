package com.example.domain.usecase

import com.example.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// 즐겨찾기한 도서의 id 목록을 가져오는 UseCase
class GetFavoriteIdsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(): Flow<Set<String>> = favoriteRepository.getFavoriteIdsFlow().map { it.toSet() }
}