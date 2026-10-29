package com.example.data.mapper

import com.example.database.FavoriteEntity
import com.example.domain.model.BookModel
import com.example.network.dto.BookDto
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

// 날짜 -> yyyy-MM-dd 변환
fun String.toFormattedDate(): String = try {
    OffsetDateTime.parse(this, DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(DateTimeFormatter.ISO_LOCAL_DATE)
} catch (e: Exception) {
    this
}

// 서버 응답 -> domain 모델
fun BookDto.toModel(): BookModel = BookModel(
    id = isbn.orEmpty().replace(" ", ""), // 공백 제거한 ISBN(ISBN10 + ISBN13)
    title = title.orEmpty(),
    contents = contents.orEmpty(),
    url = url.orEmpty(),
    isbn = isbn.orEmpty(),
    datetime = datetime.orEmpty().toFormattedDate(),
    authors = authors.orEmpty(),
    publisher = publisher.orEmpty(),
    translators = translators.orEmpty(),
    price = price,
    salePrice = sale_price,
    thumbnail = thumbnail.orEmpty(),
    status = status.orEmpty(),
)

// domain 모델 -> 즐겨찾기 DB 저장 형태
fun BookModel.toEntity(): FavoriteEntity = FavoriteEntity(
    id = id,
    title = title,
    contents = contents,
    url = url,
    isbn = isbn,
    datetime = datetime,
    authors = authors,
    publisher = publisher,
    translators = translators,
    price = price,
    sale_price = salePrice,
    thumbnail = thumbnail,
    status = status,
)

// 즐겨찾기 DB -> domain 모델
fun FavoriteEntity.toModel(): BookModel = BookModel(
    id = id,
    title = title,
    contents = contents,
    url = url,
    isbn = isbn,
    datetime = datetime,
    authors = authors,
    publisher = publisher,
    translators = translators,
    price = price,
    salePrice = sale_price,
    thumbnail = thumbnail,
    status = status,
    isFavorite = true,
)
