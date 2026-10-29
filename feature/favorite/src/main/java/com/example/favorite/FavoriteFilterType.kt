package com.example.favorite

import androidx.annotation.StringRes

// 즐겨찾기 가격 필터
enum class FavoriteFilterType(@StringRes val textRes: Int) {
    RESET(R.string.favorite_filter_reset),
    PRICE0(R.string.favorite_filter_under_10000),
    PRICE1(R.string.favorite_filter_10000),
    PRICE2(R.string.favorite_filter_20000),
    PRICE3(R.string.favorite_filter_30000),
    PRICE_OVER(R.string.favorite_filter_over_40000),
}
