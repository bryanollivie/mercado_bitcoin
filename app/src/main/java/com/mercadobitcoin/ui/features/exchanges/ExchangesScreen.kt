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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Corretoras de Bitcoin") })
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading && !state.exchanges.isEmpty(),
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {

                state.isLoading && state.exchanges.isEmpty() -> {
                    LoadingView()
                }

                state.error != null -> {
                    ErrorView(
                        message = state.error,
                        onRetry = { viewModel.refresh() }
                    )
                }

                state.exchanges.isNotEmpty() -> {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(
                            items = state.exchanges,
                            key = { it.id }
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

                else -> {
                    EmptyView()
                }
            }
        }
    }
}
