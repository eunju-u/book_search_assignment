package com.example.booksearch.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.booksearch.navigation.MainRoute
import com.example.booksearch.navigation.BottomTab
import com.example.detail.navigation.detailScreen
import com.example.detail.navigation.navigateToDetail
import com.example.domain.model.BookModel
import com.example.favorite.FavoriteScreen
import com.example.search.SearchScreen

// 앱 메인 화면
@Composable
fun BookApp(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainRoute,
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
        ) {
            composable<MainRoute> {
                MainScreen(onBookClick = { book -> navController.navigateToDetail(book) })
            }
            detailScreen(
                navController = navController,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

// 메인 화면 - 하단 탭 + 선택된 탭의 화면 (검색, 즐겨찾기)
@Composable
private fun MainScreen(onBookClick: (BookModel) -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(BottomTab.SEARCH) }
    // 탭마다 화면 상태(스크롤 위치 등)를 따로 저장하고 탭 시 복원
    val tabStateHolder = rememberSaveableStateHolder()

    Scaffold(
        bottomBar = { BookBottomBar(onTabClick = { selectedTab = it }) },
    ) { tabPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(tabPadding)
        ) {
            tabStateHolder.SaveableStateProvider(key = selectedTab.name) {
                when (selectedTab) {
                    BottomTab.SEARCH -> SearchScreen(onBookClick = onBookClick)
                    BottomTab.FAVORITE -> FavoriteScreen(onBookClick = onBookClick)
                }
            }
        }
    }
}

// 하단 탭 바
@Composable
private fun BookBottomBar(onTabClick: (BottomTab) -> Unit) {
    Surface(
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTab.entries.forEach { tab ->
                IconButton(onClick = { onTabClick(tab) }) {
                    Image(
                        painter = painterResource(id = tab.iconRes),
                        colorFilter = tab.iconTint?.let { ColorFilter.tint(it) },
                        contentDescription = stringResource(tab.labelRes),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}