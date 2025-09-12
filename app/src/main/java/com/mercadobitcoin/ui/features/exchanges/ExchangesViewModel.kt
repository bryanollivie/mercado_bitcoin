package com.mercadobitcoin.ui.features.exchanges

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.usecase.GetExchangesUseCase
import com.mercadobitcoin.util.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ExchangesViewModel @Inject constructor(
    private val getExchangesUseCase: GetExchangesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangesUiState())
    val uiState: StateFlow<ExchangesUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    // trava simples para garantir que a carga inicial só rode 1x por VM
    private var didInit = false

    init {
        Log.e("VM", "created ${hashCode()}")
        loadExchangesWithDetails()
    }

    /** Garante que a inicialização só acontece 1x por instância do VM */
    fun ensureInit() {
        Log.e("VM", "ensureInit called")
        if (didInit) return
        didInit = true
        loadExchangesWithDetails()
    }


    fun loadExchangesWithDetails() {
        Log.e("VM", "load called")
        // evita re-disparar se já está carregando
        if (loadJob?.isActive == true) return

        loadJob = viewModelScope.launch {
            getExchangesUseCase(page = 1)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .distinctUntilChanged() // se o usecase emite a mesma coisa de novo, ignore
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Erro inesperado") }
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
        loadJob?.cancel()
        didInit = true // já inicializado; evita novo “init” depois do refresh
        loadExchangesWithDetails()
    }
}

data class ExchangesUiState(
    val exchanges: List<Exchange> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
