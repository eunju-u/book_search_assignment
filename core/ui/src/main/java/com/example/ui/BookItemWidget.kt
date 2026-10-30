package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.domain.model.BookModel
import com.example.domain.model.effectivePrice
import com.example.designsystem.R as DesignR

@Composable
fun BookItemWidget(
    modifier: Modifier = Modifier,
    book: BookModel,
    onHeartClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(book.thumbnail)
            .crossfade(true).error(DesignR.drawable.img_error).build()
    )
    Row(
        modifier = modifier.fillMaxWidth().height(240.dp)
            .clip(RoundedCornerShape(15.dp)).background(Color.White)
            .clickable(onClick = onClick).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.book_image),
            modifier = Modifier.width(100.dp).height(150.dp).clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f).fillMaxHeight()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.book_label),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray),
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                )
                FavoriteIconButton(book.isFavorite, onHeartClick)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = book.title.toHyphen(),
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                )
                listOf(
                    stringResource(R.string.book_author) to book.authors.toHyphen(),
                    stringResource(R.string.book_publisher) to book.publisher.toHyphen(),
                    stringResource(R.string.book_date) to book.datetime.toHyphen(),
                ).forEach { (label, value) ->
                    Text(
                        text = "$label : $value",
                        modifier = Modifier.weight(1f),
                        color = Color.Gray,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                // 판매가가 정가보다 비싸면 정가와 할인율을 숨긴다.
                if (book.salePrice >= 0 && book.salePrice <= book.price) {
                    Text(
                        text = stringResource(R.string.book_price_won, book.price.formatPrice()),
                        color = Color.Gray, textDecoration = TextDecoration.LineThrough,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (book.price > 0 && book.salePrice in 0..book.price) {
                        val discount = (book.price - book.salePrice) * 100 / book.price
                        Text(
                            text = stringResource(R.string.book_discount_percent, discount),
                            color = Color.Red, fontSize = 12.sp,
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = stringResource(R.string.book_price_won, book.effectivePrice.formatPrice()),
                        modifier = Modifier.weight(1f, fill = false),
                        fontWeight = FontWeight.Bold, color = Color.Black,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}