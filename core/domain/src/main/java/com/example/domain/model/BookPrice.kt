package com.example.domain.model

// 판매가가 비정상적인 경우 정가 사용함
val BookModel.effectivePrice: Int
    get() = if (salePrice >= 0) salePrice else price
