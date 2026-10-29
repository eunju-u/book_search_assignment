package com.example.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.component.PopupWidget
import com.example.domain.model.BookModel
import com.example.ui.BookItemWidget
import com.example.ui.FilterButton
import com.example.ui.SortButton
import com.example.ui.TopWidget

// 팝업 enum (정렬, 필터)
private enum class FavoritePopup { SORT, FILTER }

// 즐겨찾기 화면
@Composable
fun FavoriteScreen() {
    val resources = LocalResources.current
    var sort by rememberSaveable { mutableStateOf(FavoriteSortType.ASCENDING) }
    var filter by rememberSaveable { mutableStateOf(FavoriteFilterType.RESET) }
    var popup by rememberSaveable { mutableStateOf<FavoritePopup?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopWidget(
            title = stringResource(R.string.favorite),
            text = "",
            selectedSort = stringResource(sort.textRes),
            actions = {
                FilterButton(
                    onClick = { popup = FavoritePopup.FILTER },
                    pointer = filter != FavoriteFilterType.RESET
                )
                SortButton(onClick = { popup = FavoritePopup.SORT })
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleBooks) { book ->
                BookItemWidget(book = book)
            }
        }
    }

    when (popup) {
        FavoritePopup.SORT -> PopupWidget(
            items = FavoriteSortType.entries,
            selectedItem = sort,
            getText = { resources.getString(it.textRes) },
            onItemSelected = {
                sort = it
                popup = null
            },
            onDismiss = { popup = null }
        )

        FavoritePopup.FILTER -> PopupWidget(
            items = FavoriteFilterType.entries,
            selectedItem = filter,
            getText = { resources.getString(it.textRes) },
            onItemSelected = {
                filter = it
                popup = null
            },
            onDismiss = { popup = null }
        )

        null -> {}
    }
}

private val sampleBooks = listOf(
    sampleBook("코틀린 코루틴 이해하기", listOf("김철수", "이영희"), "길벗", "2023-11-02", 32000),
    sampleBook("모바일 UI 디자인 가이드", listOf("정하늘"), "제이펍", "2024-09-30", 24000),
    sampleBook("알고리즘 문제 해결 전략", listOf("윤서준"), "인사이트", "2021-05-12", 9800),
)

private fun sampleBook(
    title: String,
    authors: List<String>,
    publisher: String,
    date: String,
    price: Int,
) = BookModel(
    title = title,
    authors = authors,
    publisher = publisher,
    datetime = date,
    price = price,
    thumbnail = "",
    isFavorite = true,
)