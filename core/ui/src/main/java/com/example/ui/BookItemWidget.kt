package com.example.ui

import com.example.domain.model.BookModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.designsystem.component.RowTextWidget
import com.example.designsystem.R as DesignR

@Composable
fun BookItemWidget(
    modifier: Modifier = Modifier,
    book: BookModel,
    onHeartClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(book.thumbnail)
            .crossfade(true)
            .error(DesignR.drawable.img_error)
            .build()
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(12.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽 이미지
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.book_image),
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 이미지 옆 제목/지은이
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.book_label),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray),
                )
                Text(
                    text = book.title.toHyphen(),
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                RowTextWidget(
                    left = stringResource(R.string.book_author), right = book.authors.toHyphen(), color = Color.Gray, maxLines = 1
                )
                RowTextWidget(left = stringResource(R.string.book_publisher), right = book.publisher.toHyphen(), color = Color.Gray, maxLines = 1)
                RowTextWidget(left = stringResource(R.string.book_date), right = book.datetime.toHyphen(), color = Color.Gray, maxLines = 1)
                Spacer(modifier = Modifier.height(50.dp))
            }
        }

        FavoriteIconButton(
            isFavorite = book.isFavorite,
            onClick = onHeartClick,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        // salePrice가(할인가)가 0 이상일 경우 정가(price), 할인율(직접계산), 할인가(salePrice가)를 표시
        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.End,
        ) {
            if (book.salePrice >= 0) {
                Text(
                    text = stringResource(R.string.book_price_won, book.price.formatPrice()),
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val discountPercent = if (book.price > 0 && book.salePrice <= book.price) {
                        (book.price - book.salePrice) * 100 / book.price
                    } else null
                    if (discountPercent != null) {
                        Text(
                            text = stringResource(R.string.book_discount_percent, discountPercent),
                            color = Color.Red,
                            fontSize = 12.sp,
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = stringResource(R.string.book_price_won, book.salePrice.formatPrice()),
                        fontWeight = FontWeight.Bold,
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.book_price_won, book.price.formatPrice()),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}