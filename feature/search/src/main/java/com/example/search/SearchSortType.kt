package com.example.search

import androidx.annotation.StringRes

// 검색 정렬
enum class SearchSortType(@StringRes val textRes: Int) {
    ACCURACY(R.string.sort_accuracy), LATEST(R.string.sort_latest),
}