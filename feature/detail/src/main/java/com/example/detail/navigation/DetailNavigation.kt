package com.example.detail.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.detail.DetailScreen
import com.example.domain.model.BookModel
import kotlinx.serialization.Serializable

@Serializable
data object DetailRoute

private const val SELECTED_BOOK_KEY = "selectedBook"

fun NavHostController.navigateToDetail(book: BookModel) {
    currentBackStackEntry?.savedStateHandle?.set(SELECTED_BOOK_KEY, book)
    navigate(DetailRoute)
}

// 상세 화면 NavHost에 등록
fun NavGraphBuilder.detailScreen(navController: NavHostController, onBack: () -> Unit) {
    composable<DetailRoute> { detailEntry ->
        val book = remember(detailEntry) {
            navController.previousBackStackEntry?.savedStateHandle?.get<BookModel>(SELECTED_BOOK_KEY)
        }
        DetailScreen(
            initialBook = book,
            onBack = {
                // 상세 화면에서 뒤로가기 시 메인 없어지는 현상 예외처리
                if (navController.currentBackStackEntry == detailEntry) {
                    onBack()
                }
            },
        )
    }
}