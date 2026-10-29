package com.example.ui

import java.util.Locale

// 빈 값이면 - 반환
fun String.toHyphen(): String = ifBlank { "-" }

// 빈 값 제외 ,로 연결하고 모두 비면 - 반환
fun List<String>.toHyphen(): String = filter { it.isNotBlank() }.joinToString(", ").toHyphen()

// 가격 천 단위 표시
fun Int.formatPrice(): String = String.format(Locale.KOREA, "%,d", this)
