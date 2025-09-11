package com.mercadobitcoin.ui.features.exchanges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.usecase.GetExchangesUseCase
import com.mercadobitcoin.util.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@HiltViewModel
class ExchangesViewModel @Inject constructor(
    private val getExchangesUseCase: GetExchangesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangesUiState())
    val uiState: StateFlow<ExchangesUiState> = _uiState.asStateFlow()

    init {
        loadExchangesWithDetails()
    }

    fun loadExchangesWithDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getExchangesUseCase(page = 1).collectLatest { result ->
                _uiState.value = when (result) {
                    is AppResult.Success -> _uiState.value.copy(
                        exchanges = result.data,
                        isLoading = false,
                        error = null
                    )
                    is AppResult.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                    else -> _uiState.value
                }
            }
        }
    }

    fun loadExchanges() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getExchangesUseCase(page = 1).collectLatest { result ->
                _uiState.value = when (result) {
                    is AppResult.Success -> _uiState.value.copy(
                        exchanges = result.data,
                        isLoading = false,
                        error = null
                    )
                    is AppResult.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                    else -> _uiState.value
                }
            }
        }
    }

    fun refresh() =  loadExchangesWithDetails()
}

data class ExchangesUiState(
    val exchanges: List<Exchange> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
/*

@HiltViewModel
class ExchangesViewModel @Inject constructor(
    private val getExchangesUseCase: GetExchangesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangesUiState())
    val uiState: StateFlow<ExchangesUiState> = _uiState

    init {
        loadExchanges()
    }

    fun loadExchanges() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getExchangesUseCase(page = 1).collectLatest { result ->
                _uiState.value = when (result) {
                    is AppResult.Success -> _uiState.value.copy(
                        exchanges = result.data,
                        isLoading = false
                    )
                    is AppResult.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                    else -> _uiState.value
                }
            }
        }
    }

}


*/

/*@HiltViewModel
class ExchangesViewModel @Inject constructor(
    private val getExchanges: GetExchangesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Flow<AppResult<List<Exchange>>>>(AppResult.Loading)
    val state: Flow<AppResult<List<Exchange>>> = _state

    private var page = 1

    fun load(initial: Boolean = false) {
        if (initial) page = 1
        viewModelScope.launch {
            _state.value = AppResult.Loading
            _state.value = getExchanges(page)
            if (_state.value is AppResult.Success) page++
        }
    }
}*/
/*
@HiltViewModel
class ExchangesViewModel @Inject constructor(
    private val getExchangesUseCase: GetExchangesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangesUiState())
    val uiState: StateFlow<ExchangesUiState> = _uiState.asStateFlow()

    fun loadExchanges() {
        viewModelScope.launch {
            getExchangesUseCase(page = 1).collect { result ->
                _uiState.value =
                    when (result)
                {
                    is AppResult.Loading -> _uiState.value.copy(isLoading = true)
                    is AppResult.Success -> _uiState.value.copy(
                        exchanges = result.data,
                        isLoading = false,
                        error = null
                    )
                    is AppResult.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    //
}

data class ExchangesUiState(
    val exchanges: List<Exchange> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)*/
