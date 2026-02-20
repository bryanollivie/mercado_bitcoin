package com.mercadobitcoin.ui.features.exchangedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mercadobitcoin.core.common.DateFormatter
import com.mercadobitcoin.core.common.NumberFormatter
import com.mercadobitcoin.core.common.WebsiteLink
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.ui.components.CurrencyChart
import com.mercadobitcoin.ui.components.ErrorView
import com.mercadobitcoin.ui.components.LoadingView

/**
 * Tela de detalhes de uma exchange.
 * Exibe informacoes completas: logo, nome, website, fees, data de lancamento
 * e a lista de criptomoedas negociadas (com grafico).
 *
 * Estados visuais:
 * - Loading: animacao de carregamento
 * - Error: mensagem de erro com botao de retry
 * - Success: card com informacoes + secao de moedas
 *
 * @param viewModel injetado pelo Hilt com o exchangeId do argumento de navegacao.
 * @param onBack callback para voltar a tela anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailScreen(
    viewModel: ExchangeDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.exchangeDetail?.name ?: "Voltar") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingView()
            state.error != null -> ErrorView(
                message = state.error,
                onRetry = { viewModel.refresh() },
            )
            state.exchangeDetail != null -> ExchangeDetailContent(
                exchange = state.exchangeDetail!!,
                onBack = onBack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        }
    }
}

/**
 * Conteudo scrollavel da tela de detalhes.
 * Renderiza um card com informacoes principais e, se houver moedas,
 * exibe um titulo + grafico de criptomoedas.
 */
@Composable
fun ExchangeDetailContent(
    exchange: ExchangeDetail,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card com informacoes principais da exchange
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = exchange.logoUrl,
                        contentDescription = "Logo de ${exchange.name}",
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(Modifier.height(12.dp))

                    Text(exchange.name, style = MaterialTheme.typography.headlineSmall)

                    Spacer(Modifier.height(8.dp))

                    exchange.websiteUrl?.let {
                        WebsiteLink(it)
                    }

                    Spacer(Modifier.height(8.dp))

                    // Taxa taker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Taker Fee: ${NumberFormatter.formatPercent(exchange.takerFee) ?: "-"}")
                    }

                    // Taxa maker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Maker Fee: ${NumberFormatter.formatPercent(exchange.makerFee) ?: "-"}")
                    }

                    // Data de lancamento
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Lançada em: ${DateFormatter.format(exchange.dateLaunched) ?: "-"}")
                    }
                }
            }
        }

        // Secao de moedas negociadas (exibida apenas se houver dados)
        if (exchange.currencies.isNotEmpty()) {
            item {
                Text("Moedas", style = MaterialTheme.typography.titleLarge)
            }

            item {
                CurrencyChart(exchange.currencies)
            }
        }
    }
}
