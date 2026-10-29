package com.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.designsystem.component.LeftImageButton
import com.example.designsystem.R as DesignR
import com.example.ui.R

// 필터 버튼
@Composable
fun FilterButton(onClick: () -> Unit, pointer: Boolean = false) {
    LeftImageButton(
        text = stringResource(R.string.filter),
        vectorImageId = DesignR.drawable.icon_filter,
        onItemClick = onClick,
        pointer = pointer
    )
}