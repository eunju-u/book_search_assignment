package com.example.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import com.example.designsystem.component.ErrorHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.designsystem.component.BackButton
import com.example.designsystem.component.RowTextWidget
import com.example.domain.model.BookModel
import com.example.designsystem.R as DesignR
import com.example.ui.R as UiR
import com.example.ui.FavoriteIconButton
import com.example.ui.toHyphen
import com.example.ui.formatPrice

// 상세 화면
@Composable
fun DetailScreen(
    initialBook: BookModel?,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val book = initialBook
    if (book == null) {
        // 책 데이터가 없을 경우 메인 화면 노출
        LaunchedEffect(Unit) { onBack() }
        return
    }

    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    val favoriteError by viewModel.favoriteError.collectAsStateWithLifecycle()
    ErrorHost(
        hasError = favoriteError,
        message = stringResource(UiR.string.favorite_update_failed),
        onErrorShown = viewModel::dismissFavoriteError,
    ) {
        DetailScreen(
            book = book,
            isFavorite = favoriteIds?.contains(book.id) ?: book.isFavorite,
            onBack = onBack,
            onFavoriteClick = { viewModel.toggleFavorite(book) },
        )
    }
}

@Composable
internal fun DetailScreen(
    book: BookModel,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(book.thumbnail)
            .crossfade(true)
            .error(DesignR.drawable.img_error)
            .build()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .wrapContentWidth(Alignment.CenterHorizontally)
            .widthIn(max = DetailMaxWidth)
            .padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackButton(onClick = onBack)
            FavoriteIconButton(isFavorite = isFavorite, onClick = onFavoriteClick)
        }

        Text(
            text = book.title.toHyphen(),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            // 왼쪽 이미지
            Image(
                painter = painter,
                contentDescription = stringResource(UiR.string.book_image),
                modifier = Modifier
                    .width(170.dp)
                    .height(250.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 이미지 옆 제목/지은이
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                RowTextWidget(
                    left = stringResource(id = UiR.string.book_author),
                    right = book.authors.toHyphen(),
                    color = Color.Black
                )
                RowTextWidget(
                    left = stringResource(id = UiR.string.book_publisher),
                    right = book.publisher.toHyphen(),
                    color = Color.Black
                )
                RowTextWidget(
                    left = stringResource(id = UiR.string.book_date),
                    right = book.datetime.toHyphen(),
                    color = Color.Black
                )
                RowTextWidget(
                    left = stringResource(id = UiR.string.book_isbn),
                    right = book.isbn.toHyphen(),
                    color = Color.Black
                )
                RowTextWidget(
                    left = stringResource(id = UiR.string.book_price),
                    right = stringResource(UiR.string.book_price_won, book.price.formatPrice()),
                    color = Color.Black
                )
                // salePrice가 0 이상일 때만 표시
                if (book.salePrice >= 0) {
                    val discountPercent = if (book.price > 0 && book.salePrice <= book.price) {
                        (book.price - book.salePrice) * 100 / book.price
                    } else null
                    RowTextWidget(
                        left = stringResource(id = UiR.string.book_sale_price),
                        right = if (discountPercent != null) {
                            stringResource(
                                UiR.string.book_sale_price_with_discount,
                                book.salePrice.formatPrice(),
                                discountPercent,
                            )
                        } else {
                            stringResource(UiR.string.book_price_won, book.salePrice.formatPrice())
                        },
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(id = UiR.string.book_info),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = book.contents.toHyphen(), color = Color.Black)
    }
}

private val DetailMaxWidth = 600.dp
