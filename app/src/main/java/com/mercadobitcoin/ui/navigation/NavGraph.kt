package com.mercadobitcoin.ui.navigation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mercadobitcoin.ui.features.exchangedetails.ExchangeDetailScreen
import com.mercadobitcoin.ui.features.exchanges.ExchangesScreen

/**
 * Definicao das rotas de navegacao do app.
 * Centraliza os nomes de rotas e argumentos para evitar strings duplicadas.
 */
object Routes {
    const val EXCHANGES_LIST = "exchange_list"
    const val EXCHANGE_DETAIL = "exchange_detail"
    const val ARG_EXCHANGE_ID = "exchangeId"
    val EXCHANGE_DETAIL_ROUTE_WITH_ARG = "$EXCHANGE_DETAIL/{$ARG_EXCHANGE_ID}"

    /** Gera a rota de detalhe com o ID codificado para URL. */
    fun exchangeDetailRoute(id: String): String = "$EXCHANGE_DETAIL/${Uri.encode(id)}"
}

/**
 * Grafo de navegacao principal do app com duas telas:
 * - [ExchangesScreen]: listagem de exchanges (tela inicial)
 * - [ExchangeDetailScreen]: detalhes de uma exchange selecionada
 *
 * O [exchangeId] e passado como argumento de rota e recuperado
 * pelo ViewModel via [SavedStateHandle].
 */
@SuppressLint("NewApi")
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.EXCHANGES_LIST
    ) {
        composable(Routes.EXCHANGES_LIST) {
            ExchangesScreen(navController = navController)
        }
        composable(
            route = Routes.EXCHANGE_DETAIL_ROUTE_WITH_ARG,
            arguments = listOf(navArgument(Routes.ARG_EXCHANGE_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            ExchangeDetailScreen(
                viewModel = hiltViewModel(backStackEntry),
                onBack = { navController.popBackStack() }
            )
        }
    }
}
