package com.example.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R as DesignR

// 즐겨찾기 하트 버튼
@Composable
fun FavoriteIconButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier.size(24.dp)) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(
                id = if (isFavorite) DesignR.drawable.icon_heart_full else DesignR.drawable.icon_heart
            ),
            contentDescription = stringResource(R.string.book_favorite),
            tint = if (isFavorite) Color.Red else Color.Gray
        )
    }
}