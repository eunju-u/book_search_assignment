package com.example.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// 왼쪽에 이미지, 오른쪽에 글자 셀
@Composable
fun LeftImageButton(
    text: String = "",
    @DrawableRes vectorImageId: Int? = null,
    onItemClick: (() -> Unit)? = null,
    pointer: Boolean = false
) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .border(shape = RoundedCornerShape(20.dp), width = 1.dp, color = Color.Gray)
            .clip(shape = RoundedCornerShape(20.dp))
            .then(if (onItemClick != null) Modifier.clickable { onItemClick() } else Modifier)
            .padding(PaddingValues(horizontal = 11.dp, vertical = 8.dp)),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (vectorImageId != null) {
                Icon(
                    modifier = Modifier.size(17.dp, 17.dp),
                    painter = painterResource(id = vectorImageId),
                    tint = Color.Black,
                    contentDescription = text
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(text = text)

            if (pointer) {
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier
                        .padding(start = 1.dp, bottom = 10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 5.dp, height = 5.dp)
                            .background(color = Color.Red, shape = CircleShape)
                    )
                }
            }
        }
    }
}