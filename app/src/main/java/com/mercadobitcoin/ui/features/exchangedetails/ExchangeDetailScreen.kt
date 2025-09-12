package com.mercadobitcoin.ui.features.exchangedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.ui.components.ErrorView
import com.mercadobitcoin.ui.components.LoadingView

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
                title = { Text("Exchange") },
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
                onRetry = { viewModel.loadExchangeDetailFull(state.exchangeDetail?.id ?: "") },
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

@Composable
fun ExchangeDetailContent(
    exchange: ExchangeDetail,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,                    // use o modifier recebido
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = exchange.logoUrl,
                    contentDescription = "Logo de ${exchange.name}",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(exchange.name, style = MaterialTheme.typography.headlineMedium)
                    Text("ID: ${exchange.id}")
                }
            }
            exchange.websiteUrl?.let { Text("Site: $it") }
            Text("Maker Fee: ${exchange.makerFee ?: "-"}")
            Text("Taker Fee: ${exchange.takerFee ?: "-"}")
            Text("Lançada em: ${exchange.dateLaunched ?: "-"}")
        }

        item { Text("Moedas", style = MaterialTheme.typography.titleLarge) }

        items(exchange.currencies.size, key = { it }) { c ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Text(c.name)
                //Text("$${c.priceUsd}")
            }
            Divider()
        }

        item {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Voltar")
                //navController.navigateUp()
            }
        }
    }
}


