package com.example.network

import com.example.network.dto.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

// 카카오 도서 검색 API
interface ApiService {
    @GET("v3/search/book")
    suspend fun getBooks(
        @Query("query") query: String, //검색어
        @Query("sort") sort: String, //정렬 방식
        @Query("page") page: Int, //페이징 페이지
        @Query("size") size: Int, //페이지당 문서 수
        @Query("target") target: String? = null, //검색 필드 제한 (title, isbn, publisher, person)
    ): BookResponse
}