package com.example.stateflowsample.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateflowsample.DataService
import com.example.stateflowsample.ServiceResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val TAG = "FormlViewModel"

class FormViewModel : ViewModel() {
    companion object {
        const val ResendOtpDelayInSec = 60
    }

    private val _state = MutableStateFlow(FormScreenState())
    val state: StateFlow<FormScreenState> = _state.asStateFlow()

    private val _actionSharedFlow = MutableSharedFlow<FormActionResult>(replay = 0)
    val actionResultSharedFlow: SharedFlow<FormActionResult> = _actionSharedFlow

    private var job: Job? = null

    fun submit() {
        if (_state.value.name.isEmpty()) {
            _state.value =
                _state.value.copy(nameError = "Enter name", phoneError = null, otpError = null)
            return
        }

        if (_state.value.phone.isEmpty()) {
            _state.value =
                _state.value.copy(phoneError = "Enter phone", nameError = null, otpError = null)
            return
        }

        if (_state.value.otp.isEmpty()) {
            _state.value =
                _state.value.copy(otpError = "Enter otp", phoneError = null, nameError = null)
            return
        }

        viewModelScope.launch {
            _actionSharedFlow.emit(FormActionResult.Initial)
            _state.value = _state.value.copy(loading = true, nameError = "", phoneError = "")
            when (val result = DataService.submitForm()) {
                is ServiceResult.Failure -> {
                    if (result.code == 409) {
                        _state.value = _state.value.copy(
                            loading = false,
                            phoneError = result.message
                        )
                    } else {
                        _state.value = _state.value.copy(loading = false)
                        _actionSharedFlow.emit(FormActionResult.Failure(result.message))
                    }
                }

                is ServiceResult.Success -> {
                    _actionSharedFlow.emit(FormActionResult.Success)
                }
            }
        }
    }

    fun sendOtp() {
        viewModelScope.launch {
            _actionSharedFlow.emit(FormActionResult.Initial)
            _state.value = _state.value.copy(otpSendLoading = true, nameError = "", phoneError = "")
            when (listOf(true, false).random()) {
                true -> {
                    _state.value = _state.value.copy(otpSendLoading = false, otpFieldVisible = true)
                    startTimer()
                }

                false -> {
                    _actionSharedFlow.emit(FormActionResult.Failure("Failed"))
                }
            }
        }
    }

    private fun startTimer() {
        job?.cancel()
        job = null
        job = viewModelScope.launch {
            (ResendOtpDelayInSec downTo 0)
                .asFlow()
                .onEach { delay(1000) }
                .onStart { emit(ResendOtpDelayInSec) }
                .conflate()
                .onCompletion {
                    job?.cancel()
                    job = null
                }
                .collectLatest { seconds ->
                    _state.value = state.value.copy(
                        otpTimerValueInSec = seconds,
                    )
                }
        }
    }

    fun onPhoneChange(phone: String) {
        _state.value = _state.value.copy(phone = phone)
        if (phone.length == 10) {
            sendOtp()
        }
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun onOtpChange(otp: String) {
        _state.value = _state.value.copy(otp = otp)
    }
}