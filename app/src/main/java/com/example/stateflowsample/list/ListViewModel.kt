package com.example.stateflowsample.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateflowsample.DataService
import com.example.stateflowsample.ServiceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ListViewModel : ViewModel() {

    private val _state = MutableStateFlow(ListScreenState())
    val state: StateFlow<ListScreenState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                loading = true,
                data = listOf(),
                failureMessage = ""
            )
            when (val result = DataService.getList()) {
                is ServiceResult.Failure -> _state.value = _state.value.copy(
                    loading = false,
                    failureMessage = result.message
                )

                is ServiceResult.Success -> _state.value = _state.value.copy(
                    loading = false,
                    data = result.data
                )
            }
        }
    }
}