package com.mercadobitcoin.ui.features.exchanges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.usecase.GetExchangesUseCase
import com.mercadobitcoin.util.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            getExchangesUseCase(page = 1)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .distinctUntilChanged()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Erro inesperado"
                        )
                    }
                }
                .collectLatest { result ->
                    when (result) {
                        is AppResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true, error = null) }
                        }

                        is AppResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    exchanges = result.data,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }

                        is AppResult.Error -> {
                            _uiState.update { it.copy(isLoading = false, error = result.message) }
                        }
                    }
                }
        }
    }

    fun refresh() {
        loadExchangesWithDetails()
    }
}

data class ExchangesUiState(
    val exchanges: List<Exchange> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
