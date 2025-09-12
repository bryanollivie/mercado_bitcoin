package com.mercadobitcoin.ui.features.exchangedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadobitcoin.data.repository.ExchangeRepository
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.ui.navigation.Routes
import com.mercadobitcoin.util.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ExchangeRepository
) : ViewModel() {

    private val exchangeId: String = checkNotNull(savedStateHandle[Routes.ARG_EXCHANGE_ID])
    private val _uiState = MutableStateFlow(ExchangeDetailUiState())
    val uiState: StateFlow<ExchangeDetailUiState> = _uiState.asStateFlow()

    init {
        loadExchangeDetailFull(exchangeId)
    }

    fun loadExchangeDetailFull(exchangeId: String) {
        viewModelScope.launch {
            repository.getExchangeDetail(exchangeId)
                .collect { result ->
                    _uiState.value = when (result) {
                        is AppResult.Success -> _uiState.value.copy(
                            exchangeDetail = result.data,
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

    fun refresh() {
        loadExchangeDetailFull(exchangeId)
    }
}

data class ExchangeDetailUiState(
    val exchangeDetail: ExchangeDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
