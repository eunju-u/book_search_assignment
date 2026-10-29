package com.example.database

import androidx.room.TypeConverter

// FavoriteEntity에 있는 데이터 Room에 저장시 List<String> <-> 문자열 로 저장/반환
class Converter {

    // DB에서 읽을 경우 문자열 -> 리스트
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    // DB에 저장할 경우 리스트 -> 문자열
    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}