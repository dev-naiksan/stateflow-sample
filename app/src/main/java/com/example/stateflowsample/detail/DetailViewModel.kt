package com.example.stateflowsample.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateflowsample.DataService
import com.example.stateflowsample.ServiceResult
import com.example.stateflowsample.list.ListAct
import com.example.stateflowsample.list.ListModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val item = requireNotNull(savedStateHandle.get<ListModel>(ListAct.LIST_ITEM))

    private val _state = MutableStateFlow(DetailScreenState())
    val state: StateFlow<DetailScreenState> = _state.asStateFlow()

    init {
        _state.value = _state.value.copy(
            title = item.name
        )
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, failureMessage = "")
            when (val result = DataService.getDetail(item.id)) {
                is ServiceResult.Failure -> _state.value = _state.value.copy(
                    loading = false,
                    failureMessage = result.message
                )

                is ServiceResult.Success -> _state.value = _state.value.copy(
                    loading = false,
                    description = result.data.description
                )
            }
        }
    }
}