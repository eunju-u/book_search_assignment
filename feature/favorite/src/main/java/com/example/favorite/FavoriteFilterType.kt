package com.example.favorite

import androidx.annotation.StringRes

// 즐겨찾기 가격 필터
enum class FavoriteFilterType(@StringRes val textRes: Int, val range: IntRange?) {
    RESET(R.string.favorite_filter_reset, null),
    PRICE0(R.string.favorite_filter_under_10000, 0..9999),
    PRICE1(R.string.favorite_filter_10000, 10000..19999),
    PRICE2(R.string.favorite_filter_20000, 20000..29999),
    PRICE3(R.string.favorite_filter_30000, 30000..39999),
    PRICE_OVER(R.string.favorite_filter_over_40000, 40000..Int.MAX_VALUE),
}
