package com.example.stateflowsample.list

data class ListScreenState(
    val data: List<ListModel> = listOf(),
    val failureMessage: String = "",
    val loading: Boolean = false
)