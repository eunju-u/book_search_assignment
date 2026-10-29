package com.example.data.dataSource

import com.example.domain.error.DataError
import com.example.network.ApiService
import com.example.network.dto.BookResponse
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// 카카오 도서 검색 API 호출 담당
class BookDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    // 실패하면 원인에 따라 DataError(네트워크 / 서버 / 언노운)로 반환
    suspend fun getBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int,
    ): BookResponse = try {
        apiService.getBooks(query, sort, page, size)
    } catch (e: IOException) {
        throw DataError.Network(e)
    } catch (e: HttpException) {
        throw DataError.Server(e.code(), e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw DataError.Unknown(e)
    }
}
