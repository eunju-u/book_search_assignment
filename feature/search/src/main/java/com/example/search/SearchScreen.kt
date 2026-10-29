package com.example.search

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
import com.example.ui.SortButton
import com.example.ui.TopWidget

// 검색 화면
@Composable
fun SearchScreen() {
    val resources = LocalResources.current
    var sort by rememberSaveable { mutableStateOf(SearchSortType.ACCURACY) }
    var showSort by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopWidget(
            title = stringResource(R.string.search),
            text = "",
            selectedSort = stringResource(sort.textRes),
            actions = {
                SortButton(onClick = { showSort = true })
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(sampleBooks) { book ->
                BookItemWidget(book = book)
            }
        }
    }

    if (showSort) {
        PopupWidget(
            items = SearchSortType.entries,
            selectedItem = sort,
            onItemSelected = {
                sort = it
                showSort = false
            },
            onDismiss = { showSort = false },
            getText = { resources.getString(it.textRes) }
        )
    }
}

private val sampleBooks = listOf(
    sampleBook("안드로이드 앱 개발 입문", listOf("홍길동"), "한빛출판", "2024-03-15", 28000),
    sampleBook("코틀린 코루틴 이해하기", listOf("김철수", "이영희"), "길벗", "2023-11-02", 32000, isFavorite = true),
    sampleBook("Jetpack Compose 실전", listOf("박민수"), "인사이트", "2025-01-20", 35000),
    sampleBook("클린 아키텍처 따라하기", listOf("최지은"), "위키북스", "2022-07-08", 27000),
    sampleBook("모바일 UI 디자인 가이드", listOf("정하늘"), "제이펍", "2024-09-30", 24000, isFavorite = true),
)

private fun sampleBook(
    title: String,
    authors: List<String>,
    publisher: String,
    date: String,
    price: Int,
    isFavorite: Boolean = false,
) = BookModel(
    title = title,
    authors = authors,
    publisher = publisher,
    datetime = date,
    price = price,
    thumbnail = "",
    isFavorite = isFavorite,
)