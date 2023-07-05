package com.example.stateflowsample.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailScreenState(
    val title: String = "",
    val description: String = "",
    val loading: Boolean = false,
    val failureMessage: String = ""
) : Parcelable