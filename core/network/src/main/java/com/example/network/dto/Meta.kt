package com.example.network.dto

data class Meta(
    val total_count: Int, // 검색된 문서 수
    val pageable_count: Int, //중복 문서 제외, 노출 가능 문서 수
    val is_end: Boolean // 현재 페이지가 마지막 페이지인지 여부
)