package com.mercadobitcoin.ui.features.exchangedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.domain.usecase.GetExchangeCurrenciesUseCase
import com.mercadobitcoin.domain.usecase.GetExchangeDetailUseCase
import com.mercadobitcoin.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel da tela de detalhes de uma exchange.
 * Carrega informacoes da exchange e encadeia a busca de moedas usando [flatMapLatest].
 *
 * Fluxo de carregamento:
 * 1. Busca detalhe da exchange via [GetExchangeDetailUseCase]
 * 2. Se sucesso, atualiza UI com os dados parciais e encadeia [GetExchangeCurrenciesUseCase]
 * 3. Ao receber moedas, atualiza o detalhe com a lista completa
 *
 * O [exchangeId] e extraido do [SavedStateHandle] via argumento de navegacao.
 */
@HiltViewModel
class ExchangeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExchangeDetailUseCase: GetExchangeDetailUseCase,
    private val getExchangeCurrenciesUseCase: GetExchangeCurrenciesUseCase
) : ViewModel() {

    private val exchangeId: String = checkNotNull(savedStateHandle[Routes.ARG_EXCHANGE_ID])
    private val _uiState = MutableStateFlow(ExchangeDetailUiState())
    val uiState: StateFlow<ExchangeDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadExchangeDetailFull(exchangeId)
    }

    /**
     * Carrega detalhe da exchange e encadeia a busca de moedas.
     * Cancela o job anterior para evitar requests duplicados em caso de refresh rapido.
     *
     * Usa [flatMapLatest] para encadear os dois flows sequencialmente:
     * - O primeiro flow (detalhe) emite Success -> dispara o segundo flow (moedas)
     * - Se o detalhe falhar, emite erro e retorna [emptyFlow] (nao busca moedas)
     *
     * @param exchangeId ID da exchange na CoinMarketCap.
     */
    fun loadExchangeDetailFull(exchangeId: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            getExchangeDetailUseCase(exchangeId)
                .flatMapLatest { result ->
                    when (result) {
                        is AppResult.Success -> {
                            // Atualiza UI com dados parciais (sem moedas ainda)
                            _uiState.value = _uiState.value.copy(
                                exchangeDetail = result.data,
                                isLoading = true,
                                error = null,
                            )
                            // Encadeia busca de moedas
                            getExchangeCurrenciesUseCase(result.data.id)
                        }
                        is AppResult.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                            emptyFlow()
                        }
                        else -> emptyFlow()
                    }
                }
                .collect { currenciesResult ->
                    // Atualiza o detalhe com as moedas recebidas
                    _uiState.value = when (currenciesResult) {
                        is AppResult.Success -> _uiState.value.copy(
                            exchangeDetail = _uiState.value.exchangeDetail?.copy(
                                currencies = currenciesResult.data
                            ),
                            isLoading = false
                        )
                        is AppResult.Error -> _uiState.value.copy(
                            isLoading = false,
                            error = currenciesResult.message
                        )
                        else -> _uiState.value
                    }
                }
        }
    }

    /** Recarrega todos os dados da exchange (detalhe + moedas). */
    fun refresh() {
        loadExchangeDetailFull(exchangeId)
    }
}

/**
 * Estado imutavel da tela de detalhes da exchange.
 *
 * @param exchangeDetail dados completos da exchange (incluindo moedas apos carregamento).
 * @param isLoading indica se ha um carregamento em andamento.
 * @param error mensagem de erro, ou null se nao ha erro.
 */
data class ExchangeDetailUiState(
    val exchangeDetail: ExchangeDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
