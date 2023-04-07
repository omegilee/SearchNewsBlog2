package com.example.searchnewsblog.data.exception

sealed class RemoteApiException(e: Throwable) : Throwable(e) {
    data class HttpException(val throwable: Throwable, val httpCode: Int, val code: String, val msg: String) : RemoteApiException(throwable)
    data class OtherException(val throwable: Throwable) : RemoteApiException(throwable)
}
