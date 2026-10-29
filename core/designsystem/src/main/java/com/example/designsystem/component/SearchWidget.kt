package com.example.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.designsystem.R

// 검색창
@Composable
fun SearchWidget(
    text: String,
    hint: String,
    onSearch: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    // 글자 유지
    var textFieldState by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(text)) }

    val pureGrayColor = Color(0xFFF5F5F5)
    val grayColor = Color.Gray
    val darkColor = Color.DarkGray
    var iconColor by remember { mutableStateOf(pureGrayColor) }
    var isShowClose by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .padding(PaddingValues(horizontal = 15.dp))
            .background(color = pureGrayColor, shape = RoundedCornerShape(15.dp))
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(PaddingValues(horizontal = 10.dp, vertical = 12.dp))
            .onFocusChanged {
                //포커스 되면 검색 아이콘 색상이 변경, 닫기 버튼 노출
                iconColor = if (it.hasFocus) darkColor else grayColor
                isShowClose = it.hasFocus
                if (!it.hasFocus) {
                    keyboardController?.hide()
                }
            },
        color = pureGrayColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier.size(24.dp, 24.dp),
                painter = painterResource(id = R.drawable.icon_search),
                tint = iconColor,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))

            BasicTextField(
                modifier = Modifier.weight(1f),
                value = textFieldState,
                onValueChange = { textFieldState = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        onSearch(textFieldState.text)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                textStyle = MaterialTheme.typography.labelSmall.copy(color = darkColor),
                cursorBrush = SolidColor(LocalContentColor.current),
                decorationBox = { innerTextField ->
                    if (textFieldState.text.isEmpty()) {
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.labelSmall,
                            color = grayColor
                        )
                    }
                    innerTextField()
                }
            )

            //검색 닫기 뷰
            if (isShowClose) {
                Icon(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .clip(shape = RoundedCornerShape(50.dp))
                        .clickable {
                            //키보드 내리기
                            keyboardController?.hide()
                            //포커스 강제 해제
                            focusManager.clearFocus()
                        },
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_circle_close),
                    contentDescription = null,
                    tint = grayColor
                )
            }
        }
    }
}