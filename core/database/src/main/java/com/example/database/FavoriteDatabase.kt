package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// 즐겨찾기 Room DB - 저자/번역자가 목록은 Converter로 저장
@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class FavoriteDatabase : RoomDatabase() {
    // 즐겨찾기 DAO
    abstract fun favoriteDao(): FavoriteDao
}