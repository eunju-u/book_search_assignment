package com.example.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RowTextWidget(
    left: String,
    right: String,
    color: Color,
    maxLines: Int = Int.MAX_VALUE,
) {
    Row{
        Text(text = left, color = color, fontWeight = FontWeight.Bold)
        Text(text = " : ", color = color)
        Text(text = right, color = color, maxLines = maxLines, overflow = TextOverflow.Ellipsis)
    }
}