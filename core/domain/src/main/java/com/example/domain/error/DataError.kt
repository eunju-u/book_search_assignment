package com.example.domain.error

// 데이터 처리 중 발생한 오류를 네트워크, 서버, 기타 로 구분
sealed class DataError(cause: Throwable) : Exception(cause) {
    class Network(cause: Throwable) : DataError(cause)
    class Server(val code: Int, cause: Throwable) : DataError(cause)
    class Unknown(cause: Throwable) : DataError(cause)
}