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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.ui.components.EmptyView
import com.mercadobitcoin.ui.components.ErrorView
import com.mercadobitcoin.ui.components.LoadingView
import com.mercadobitcoin.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExchangesScreen(
    navController: NavController,
    viewModel: ExchangesViewModel = hiltViewModel()
) {
    // lifecycle-aware
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Corretoras de Bitcoin") })
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,            // mostra spinner do pull-to-refresh
            onRefresh = { viewModel.refresh() },       // dispara refresh (sem LaunchedEffect)
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // 1) Loading inicial (sem dados ainda) → mostra loading
                state.isLoading && state.exchanges.isEmpty() -> {
                    LoadingView() // coloque um shimmer/skeleton se quiser
                }

                // 2) Erro
                state.error != null -> {
                    ErrorView(
                        message = state.error,
                        onRetry = { viewModel.refresh() }
                    )
                }

                // 3) Lista
                state.exchanges.isNotEmpty() -> {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(
                            items = state.exchanges,
                            key = { it.id } // ajuda a estabilidade da lista
                        ) { exchange ->
                            ExchangeItem(
                                exchange = exchange,
                                onClick = {
                                    navController.navigate(Routes.exchangeDetailRoute(exchange.id)
                                    )
                                }
                            )
                        }
                    }
                }

                // 4) Vazio (sem erro e sem loading)
                else -> {
                    EmptyView()
                }
            }
        }
    }
}

/*@Composable
fun ExchangesScreen(
    navController: NavController,
    viewModel: ExchangesViewModel = hiltViewModel()
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
            onRefresh = { *//*viewModel.loadExchangesWithDetails()*//* },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.error != null -> ErrorView(
                    message = state.error,
                    onRetry = { *//*viewModel.loadExchangesWithDetails()*//* }
                )

                state.exchanges.isEmpty() -> EmptyView()
                else ->
                    //Exibe o ExchangeList
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.exchanges) { exchange ->

                        ExchangeItem(
                            exchange = exchange,
                            onClick = {
                                // navega para a rota com o parâmetro id
                                //navController.navigate("exchange_detail/${exchange.id}")
                                navController.navigate(Routes.exchangeDetailRoute(exchange.id))
                            }
                        )
                    }
                }
            }
        }
    }
}*/

/*
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExchangeList(
    exchanges: List<Exchange>,
    onExchangeClick: (Exchange) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(exchanges) { exchange ->
            //ExchangeItem(exchange, onClick = { onExchangeClick(exchange) })
            ExchangeItem(
                exchange = exchange,
                onClick = {
                    // navega para a rota com o parâmetro id
                    navController.navigate("exchangeDetail/${exchange.id}")
                }
            )
        }
    }
}
*/
