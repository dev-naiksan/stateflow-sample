package com.example.stateflowsample.form

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FormScreenState(
    val name: String = "",
    val phone: String = "",
    val loading: Boolean = false,
    val nameError: String = "",
    val phoneError: String = ""
) : Parcelable