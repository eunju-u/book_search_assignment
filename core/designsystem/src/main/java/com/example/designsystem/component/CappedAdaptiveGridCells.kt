package com.example.designsystem.component

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

// 화면 전체 크기가 아니라 패딩을 제외한 그리드의 실제 너비를 기준으로 계산
data class CappedAdaptiveGridCells(
    private val minSize: Dp,
    private val maxColumns: Int,
) : GridCells {
    init {
        require(minSize.value.isFinite() && minSize.value > 0)
        require(maxColumns > 0)
    }

    override fun Density.calculateCrossAxisCellSizes(availableSize: Int, spacing: Int): List<Int> {
        val minSizePx = minSize.roundToPx().coerceAtLeast(1)
        val columns = ((availableSize + spacing) / (minSizePx + spacing))
            .coerceIn(1, maxColumns)
        val contentSize = (availableSize - spacing * (columns - 1)).coerceAtLeast(0)
        val cellSize = contentSize / columns
        val remainder = contentSize % columns
        return List(columns) { index -> cellSize + if (index < remainder) 1 else 0 }
    }
}