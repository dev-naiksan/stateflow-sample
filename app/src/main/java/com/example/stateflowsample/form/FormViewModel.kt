package com.example.stateflowsample.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateflowsample.DataService
import com.example.stateflowsample.ServiceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "FormlViewModel"

class FormViewModel : ViewModel() {

    private val _state = MutableStateFlow(FormScreenState())
    val state: StateFlow<FormScreenState> = _state.asStateFlow()

    private val _actionStateFlow = MutableStateFlow<FormActionResult>(FormActionResult.Initial)
    val actionResultStateFlow = _actionStateFlow.asStateFlow()

    fun submit() {
        if (_state.value.name.isEmpty()) {
            _state.value = _state.value.copy(nameError = "Enter name", phoneError = "")
            return
        }

        if (_state.value.phone.isEmpty()) {
            _state.value = _state.value.copy(phoneError = "Enter phone", nameError = "")
            return
        }

        viewModelScope.launch {
            _actionStateFlow.value = FormActionResult.Initial
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
                        _actionStateFlow.value = FormActionResult.Failure(result.message)
                    }
                }

                is ServiceResult.Success -> {
                    _actionStateFlow.value = FormActionResult.Success
                }
            }
        }
    }

    fun onPhoneChange(phone: String) {
        _state.value = _state.value.copy(phone = phone)
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name)
    }
}