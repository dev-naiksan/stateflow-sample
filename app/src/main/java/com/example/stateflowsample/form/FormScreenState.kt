package com.example.stateflowsample.form

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FormScreenState(
    val name: String = "",
    val phone: String = "",
    val otp: String = "",
    val otpFieldVisible: Boolean = false,
    val otpTimerValueInSec: Int = 0,
    val loading: Boolean = false,
    val otpSendLoading: Boolean = false,
    val nameError: String? = null,
    val phoneError: String? = null,
    val otpError: String? = null,
) : Parcelable