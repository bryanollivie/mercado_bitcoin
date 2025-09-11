package com.mercadobitcoin.ui.features.exchanges

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.ui.components.EmptyView
import com.mercadobitcoin.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExchangesScreen(
    viewModel: ExchangesViewModel = hiltViewModel(),
    onExchangeClick: (Exchange) -> Unit
) {

    val state by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Corretoras de Bitcoin") },
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.loadExchanges() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.error != null -> ErrorView(
                    message = state.error,
                    onRetry = { viewModel.loadExchanges() }
                )

                state.exchanges.isEmpty() -> EmptyView()
                else -> ExchangeList(
                    exchanges = state.exchanges,
                    onExchangeClick = onExchangeClick
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExchangeList(
    exchanges: List<Exchange>,
    onExchangeClick: (Exchange) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(exchanges) { exchange ->
            ExchangeItem(exchange, onClick = { onExchangeClick(exchange) })
        }
    }
}
