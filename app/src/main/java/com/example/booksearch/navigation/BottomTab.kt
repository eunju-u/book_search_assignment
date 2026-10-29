package com.example.booksearch.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.booksearch.R
import com.example.designsystem.R as DesignR

// 하단 탭
enum class BottomTab(
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int,
    val iconTint: Color?,
) {
    SEARCH(
        iconRes = DesignR.drawable.icon_search,
        labelRes = R.string.tab_search,
        iconTint = Color.Black
    ),
    FAVORITE(
        iconRes = DesignR.drawable.icon_favorite,
        labelRes = R.string.tab_favorite,
        iconTint = null
    ),
}