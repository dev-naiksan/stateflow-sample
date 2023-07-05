package com.example.stateflowsample.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListModel(
    val id: String,
    val name: String
) : Parcelable