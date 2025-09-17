package com.mercadobitcoin.ui.features.exchanges

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
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
    var query by remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Exchanges de Bitcoin") }) },
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

                else -> {
                    Column {
                       // Campo de busca + botão
                        /*OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            placeholder = { Text("Pesquisar exchanges...") },
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    viewModel.searchExchanges(query)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Buscar"
                                    )
                                }
                            }
                        )*/

                        //aplica filtro
                        val filteredExchanges = state.exchanges
                            .distinctBy { it.id }
                            .filter { it.name.contains(state.searchQuery, ignoreCase = true) }

                        if (filteredExchanges.isEmpty()) {
                            // Quando não houver dados, renderiza a EmptyView
                            EmptyView(onBack = { query = "" })
                        } else {
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

                                    //paginação
                                    if (exchange == filteredExchanges.lastOrNull() && !state.isLoading) {
                                        viewModel.loadNextPage()
                                    }
                                }
                                if (state.isLoading && filteredExchanges.isNotEmpty()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //mostrar Snackbar quando dados vierem do cache
    LaunchedEffect(state.fromCache) {
        if (state.fromCache) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Problemas no servidor, dados recuperados do cache",
                    actionLabel = "Recarregar",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.refresh()
                }
            }
        }
    }
}

