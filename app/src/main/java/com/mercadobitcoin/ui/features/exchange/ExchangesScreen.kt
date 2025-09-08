package com.mercadobitcoin.ui.features.exchange

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mercadobitcoin.ui.components.EmptyView
import com.mercadobitcoin.ui.components.ErrorView
import com.mercadobitcoin.ui.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showSystemUi = true)
@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!",
        Modifier
            .padding(80.dp) // Outer padding; outside background
            .background(color = Color.Cyan) // Solid element background color
            .padding(16.dp) // Inner padding; inside background, around text)
    )
}
/*@Preview
@Composable
fun ExchangesScreen(
    onExchangeClick: (String) -> Unit,
    //viewModel: ExchangesViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        *//*items(exchanges) { exchange ->
            ExchangeItem(
                exchange = exchange,
                onClick = { onExchangeClick(exchange) }
            )
        }*//*
    }
    //val uiState by viewModel.uiState.collectAsState()
    *//*val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("CMC Exchanges") },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isLoading),
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            *//**//*when {
                uiState.isLoading && uiState.exchanges.isEmpty() -> {
                    LoadingView()
                }
                uiState.error != null && uiState.exchanges.isEmpty() -> {
                    ErrorView(
                        message = uiState.error,
                        onRetry = { viewModel.refresh() }
                    )
                }
                uiState.exchanges.isEmpty() -> {
                    EmptyView(message = "No exchanges available")
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.exchanges,
                            key = { it.id }
                        ) { exchange ->
                            ExchangeItem(
                                exchange = exchange,
                                onClick = { onExchangeClick(exchange.id) }
                            )
                        }
                    }
                }
            }*//**//*
        }
    }*//*
}*/
