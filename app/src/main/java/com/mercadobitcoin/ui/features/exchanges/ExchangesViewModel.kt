package com.mercadobitcoin.ui.features.exchanges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.usecase.GetExchangesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel da tela principal de listagem de exchanges.
 * Gerencia o estado da UI (loading, lista, erro, cache) e controla
 * paginacao, busca e pull-to-refresh.
 *
 * Utiliza [StateFlow] para expor o estado de forma reativa ao Compose.
 * Cancela jobs anteriores ao iniciar novas cargas para evitar concorrencia.
 */
@HiltViewModel
class ExchangesViewModel @Inject constructor(
    private val getExchangesUseCase: GetExchangesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangesUiState())
    val uiState: StateFlow<ExchangesUiState> = _uiState.asStateFlow()

    private var currentPage = 1
    private var isLoadingMore = false
    private var hasMorePages = true
    private var loadJob: Job? = null

    init {
        loadExchanges(page = currentPage)
    }

    /**
     * Carrega exchanges da pagina especificada.
     * Cancela qualquer carregamento anterior para evitar sobreposicao de resultados.
     *
     * Fluxo de emissao do UseCase:
     * - Loading -> exibe indicador de carregamento
     * - Success -> atualiza a lista (substitui se refresh/page 1, concatena se paginacao)
     * - Error -> exibe mensagem de erro na UI
     *
     * @param page pagina a carregar (1-based).
     * @param isRefresh se true, reseta a lista existente e forca recarregamento.
     */
    private fun loadExchanges(page: Int, isRefresh: Boolean = false) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (isLoadingMore && !isRefresh) return@launch
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

    /**
     * Carrega a proxima pagina de exchanges (paginacao infinita).
     * Ignora se ja esta carregando ou se nao ha mais paginas disponiveis.
     */
    fun loadNextPage() {
        if (!hasMorePages || isLoadingMore) return
        currentPage++
        loadExchanges(currentPage)
    }

    /**
     * Filtra exchanges pelo nome com base na query fornecida.
     * Busca sempre a primeira pagina e aplica o filtro client-side nos resultados.
     *
     * @param query texto de busca para filtrar por nome da exchange (case-insensitive).
     */
    fun searchExchanges(query: String) {
        viewModelScope.launch {
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

    /**
     * Reseta a paginacao e recarrega tudo do zero.
     * Chamado pelo pull-to-refresh e pelo botao de retry.
     */
    fun refresh() {
        currentPage = 1
        hasMorePages = true
        isLoadingMore = false
        loadExchanges(currentPage, isRefresh = true)
    }
}

/**
 * Estado imutavel da tela de listagem de exchanges.
 *
 * @param exchanges lista de exchanges exibidas atualmente.
 * @param isLoading indica se ha um carregamento em andamento.
 * @param error mensagem de erro, ou null se nao ha erro.
 * @param searchQuery query de busca ativa (filtro client-side).
 * @param fromCache indica se os dados atuais vieram do cache local.
 */
data class ExchangesUiState(
    val exchanges: List<Exchange> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val fromCache: Boolean = false
)
