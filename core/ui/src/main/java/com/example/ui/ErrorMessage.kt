package com.example.ui

import androidx.annotation.StringRes
import com.example.domain.error.DataError

// 조회 실패 원인에 따른 안내 문구
@StringRes
fun Throwable.errorMessageRes(): Int = when (this) {
    is DataError.Network -> R.string.connect_network
    is DataError.Server -> R.string.server_error
    else -> R.string.unknown_error
}