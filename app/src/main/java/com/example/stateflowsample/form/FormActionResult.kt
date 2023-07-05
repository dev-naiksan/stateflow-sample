package com.example.stateflowsample.form

sealed interface FormActionResult {
    data class Failure(
        val message: String
    ) : FormActionResult

    object Success : FormActionResult
    object Initial : FormActionResult
}