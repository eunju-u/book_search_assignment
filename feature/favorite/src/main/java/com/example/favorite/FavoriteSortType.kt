package com.example.favorite

import androidx.annotation.StringRes

// 즐겨찾기 정렬
enum class FavoriteSortType(@StringRes val textRes: Int) {
    ASCENDING(R.string.favorite_sort_ascending),
    DESCENDING(R.string.favorite_sort_descending),
}