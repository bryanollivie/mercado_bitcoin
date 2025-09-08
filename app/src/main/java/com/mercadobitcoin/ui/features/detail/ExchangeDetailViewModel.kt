/*
package com.mercadobitcoin.ui.feat.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.domain.usecase.GetExchangeCurrenciesUseCase
import com.mercadobitcoin.domain.usecase.GetExchangeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExchangeDetailUseCase: GetExchangeDetailUseCase,
    private val getExchangeCurrenciesUseCase: GetExchangeCurrenciesUseCase
) : ViewModel() {

    private val exchangeId: String = savedStateHandle.get<String>("exchangeId") ?: ""

    private val _uiState = MutableStateFlow(ExchangeDetailUiState())
    val uiState: StateFlow<ExchangeDetailUiState> = _uiState.asStateFlow()

    init {
        loadExchangeDetails()
    }

    private fun loadExchangeDetails() {
        viewModelScope.launch {
            // Load exchange detail
            launch {
                getExchangeDetailUseCase(exchangeId).collect { result ->
                    _uiState.value = when (result) {
                        is Result.Loading -> _uiState.value.copy(isLoadingDetail = true)
                        is Result.Success -> _uiState.value.copy(
                            exchangeDetail = result.data,
                            isLoadingDetail = false,
                            detailError = null
                        )
                        is Result.Error -> _uiState.value.copy(
                            isLoadingDetail = false,
                            detailError = result.exception.message
                        )
                    }
                }
            }

            // Load currencies
            launch {
                getExchangeCurrenciesUseCase(exchangeId).collect { result ->
                    _uiState.value = when (result) {
                        is Result.Loading -> _uiState.value.copy(isLoadingCurrencies = true)
                        is Result.Success -> _uiState.value.copy(
                            currencies = result.data,
                            isLoadingCurrencies = false,
                            currenciesError = null
                        )
                        is Result.Error -> _uiState.value.copy(
                            isLoadingCurrencies = false,
                            currenciesError = result.exception.message
                        )
                    }
                }
            }
        }
    }

    fun retry() {
        loadExchangeDetails()
    }
}

data class ExchangeDetailUiState(
    val exchangeDetail: ExchangeDetail? = null,
    val currencies: List<CurrencyQuote> = emptyList(),
    val isLoadingDetail: Boolean = false,
    val isLoadingCurrencies: Boolean = false,
    val detailError: String? = null,
    val currenciesError: String? = null
)*/
