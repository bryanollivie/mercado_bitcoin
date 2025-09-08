/*
package com.mercadobitcoin.ui.feat.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.usecase.GetExchangesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mercadobitcoin.util.Result

@HiltViewModel
class ExchangesViewModel @Inject constructor(
    private val getExchangesUseCase: GetExchangesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangesUiState())
    val uiState: StateFlow<ExchangesUiState> = _uiState.asStateFlow()

    init {
        loadExchanges()
    }

    fun loadExchanges() {
        viewModelScope.launch {
            getExchangesUseCase(page = 1).collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> _uiState.value.copy(isLoading = true, error = null)
                    is Result.Success -> _uiState.value.copy(
                        exchanges = result.data,
                        isLoading = false,
                        error = null
                    )
                    is Result.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun refresh() {
        loadExchanges()
    }
}

data class ExchangesUiState(
    val exchanges: List<Exchange> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)*/
