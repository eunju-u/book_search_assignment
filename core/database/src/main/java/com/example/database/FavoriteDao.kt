package com.example.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// 즐겨찾기 테이블 조회/추가/삭제
@Dao
interface FavoriteDao {
    // 전체
    @Query("SELECT * FROM favorite_book")
    fun getAllFavoriteFlow(): Flow<List<FavoriteEntity>>

    // id 목록
    @Query("SELECT id FROM favorite_book")
    fun getAllIdsFlow(): Flow<List<String>>

    // id로 하나 찾기
    @Query("SELECT * FROM favorite_book WHERE id = :bookId LIMIT 1")
    suspend fun getById(bookId: String): FavoriteEntity?

    // 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavoriteEntity)

    // id로 삭제
    @Query("DELETE FROM favorite_book WHERE id = :bookId")
    suspend fun deleteById(bookId: String)
}