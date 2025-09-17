package com.mercadobitcoin.ui.features.exchanges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.usecase.GetExchangesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
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

    private var currentPage = 1
    private var isLoadingMore = false
    private var hasMorePages = true

    init {
        loadExchanges(page = currentPage)
    }

    private fun loadExchanges(page: Int, isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isLoadingMore) return@launch
            isLoadingMore = true

            getExchangesUseCase(page)
                .onStart {
                    if (isRefresh || page == 1) {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Erro inesperado"
                        )
                    }
                    isLoadingMore = false
                }
                .collectLatest { result ->
                    when (result) {
                        is AppResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true, error = null) }
                        }

                        is AppResult.Success -> {
                            val newList = if (isRefresh || page == 1) {
                                result.data
                            } else {
                                _uiState.value.exchanges + result.data
                            }

                            hasMorePages = result.data.isNotEmpty()

                            _uiState.update {
                                it.copy(
                                    exchanges = newList,
                                    isLoading = false,
                                    error = null,
                                    fromCache = result.fromCache ?: false
                                )
                            }
                        }

                        is AppResult.Error -> {
                            _uiState.update { it.copy(isLoading = false, error = result.message) }
                        }
                    }
                    isLoadingMore = false
                }
        }
    }

    fun loadNextPage() {
        if (!hasMorePages || isLoadingMore) return
        currentPage++
        loadExchanges(currentPage)
    }

    /**
     * 🔹 Busca explícita, chamada só ao clicar na lupa
     */
    fun searchExchanges(query: String) {
        viewModelScope.launch {
            // só dispara se mudou de fato
            //if (query == _uiState.value.searchQuery) return@launch

            //_uiState.update { it.copy(isLoading = true, error = null, searchQuery = query) }

            getExchangesUseCase(page = 1)
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
                        is AppResult.Success -> {
                            val filtered = result.data.filter {
                                it.name.contains(query, ignoreCase = true)
                            }
                            _uiState.update {
                                it.copy(
                                    exchanges = filtered,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }

                        is AppResult.Error -> {
                            _uiState.update { it.copy(isLoading = false, error = result.message) }
                        }

                        else -> Unit
                    }
                }
        }
    }


    fun refresh() {
        currentPage = 1
        hasMorePages = true
        loadExchanges(currentPage, isRefresh = true)
    }
}

data class ExchangesUiState(
    val exchanges: List<Exchange> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val fromCache: Boolean = false
)