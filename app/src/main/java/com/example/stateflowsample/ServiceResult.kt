package com.example.stateflowsample

sealed class ServiceResult<out T> {
    data class Success<T>(
        val data: T
    ) : ServiceResult<T>()

    data class Failure(
        val message: String,
        val code: Int
    ) : ServiceResult<Nothing>()
}