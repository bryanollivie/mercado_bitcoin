package com.mercadobitcoin.ui.features.exchanges

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mercadobitcoin.ui.components.EmptyView
import com.mercadobitcoin.ui.components.ErrorView
import com.mercadobitcoin.ui.components.LoadingView
import com.mercadobitcoin.ui.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExchangesScreen(
    navController: NavController,
    viewModel: ExchangesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 🔹 Estado local para texto do campo de busca
    var query by remember { mutableStateOf("") }

    // 🔹 Estado do PullToRefreshBox
    var isRefreshing by remember { mutableStateOf(false) }

    // 🔹 Desliga o refresh quando a API terminar (sucesso ou erro)
    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Exchanges de Bitcoin") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.refresh()
            },
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
                    Column {
                        // 🔹 Campo de busca + botão
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it }, // só atualiza local
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            placeholder = { Text("Pesquisar exchanges...") },
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    viewModel.searchExchanges(query) // 👈 dispara busca única
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Buscar"
                                    )
                                }
                            }
                        )

                        // 🔹 Aplica filtro
                        val filteredExchanges = state.exchanges
                            .distinctBy { it.id }
                            .filter { it.name.contains(state.searchQuery, ignoreCase = true) }

                        LazyColumn(Modifier.fillMaxSize()) {
                            itemsIndexed(
                                filteredExchanges,
                                key = { index, item -> "${item.id}_$index" }
                            ) { _, exchange ->
                                ExchangeItem(
                                    exchange = exchange,
                                    onClick = {
                                        navController.navigate(
                                            Routes.exchangeDetailRoute(exchange.id)
                                        )
                                    }
                                )

                                // 🔹 Paginação
                                if (exchange == filteredExchanges.lastOrNull() && !state.isLoading) {
                                    viewModel.loadNextPage()
                                }
                            }
                        }
                    }
                }

                else -> {
                    EmptyView()
                }
            }
        }
    }

    // 🔹 Mostrar Snackbar quando dados vierem do cache
    LaunchedEffect(state.fromCache) {
        if (state.fromCache) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Problemas no servidor, dados recuperados do cache",
                    actionLabel = "Recarregar",
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.refresh()
                }
            }
        }
    }
}


