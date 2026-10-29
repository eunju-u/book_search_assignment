package com.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.designsystem.component.LeftImageButton
import com.example.designsystem.R as DesignR

// 정렬 버튼
@Composable
fun SortButton(onClick: () -> Unit) {
    LeftImageButton(
        text = stringResource(R.string.sort),
        vectorImageId = DesignR.drawable.icon_sort,
        onItemClick = onClick
    )
}