package kr.sjh.myschedule.data.repository

sealed class Result<out R> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Fail(val throwable: Throwable) : Result<Nothing>()

    object Loading : Result<Nothing>()
}