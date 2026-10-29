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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.designsystem.component.RowTextWidget
import com.example.designsystem.R as DesignR

// 도서 아이템
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
            .wrapContentHeight()
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

            // 이미지 도서 정보
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.book_label),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray),
                )
                Text(text = book.title.toHyphen(), fontWeight = FontWeight.Bold)
                RowTextWidget(
                    left = stringResource(R.string.book_author), right = book.authors.toHyphen(), color = Color.Gray
                )
                RowTextWidget(left = stringResource(R.string.book_publisher), right = book.publisher.toHyphen(), color = Color.Gray)
                RowTextWidget(left = stringResource(R.string.book_date), right = book.datetime.toHyphen(), color = Color.Gray)
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        FavoriteIconButton(
            isFavorite = book.isFavorite,
            onClick = onHeartClick,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        // 오른쪽 하단 가격
        Text(
            text = stringResource(R.string.book_price_won, book.price.formatPrice()),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}