package com.example.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.domain.model.BookModel
import com.example.domain.usecase.LoadBooksUseCase
import kotlinx.coroutines.CancellationException

// 페이지 불러오는 PagingSource
internal class SearchPagingSource(
    private val loadBooksUseCase: LoadBooksUseCase,
    private val query: String,
    private val sort: SearchSortType,
) : PagingSource<Int, BookModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookModel> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val pageBooks = loadBooksUseCase(query, sort.label, page, params.loadSize)
            LoadResult.Page(
                data = pageBooks.books,
                prevKey = null,
                nextKey = if (pageBooks.isEnd) null else page + 1,
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BookModel>): Int? = null

    companion object {
        const val FIRST_PAGE = 1
    }
}