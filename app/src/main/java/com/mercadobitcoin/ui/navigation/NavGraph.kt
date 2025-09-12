package com.mercadobitcoin.ui.navigation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mercadobitcoin.ui.features.exchangedetails.ExchangeDetailScreen
//import com.mercadobitcoin.ui.features.exchangedetails.ExchangeDetailScreen
import com.mercadobitcoin.ui.features.exchanges.ExchangesScreen

object Routes {
    const val EXCHANGES_LIST = "exchange_list"
    const val EXCHANGE_DETAIL = "exchange_detail"
    const val ARG_EXCHANGE_ID = "exchangeId"
    val EXCHANGE_DETAIL_ROUTE_WITH_ARG = "$EXCHANGE_DETAIL/{$ARG_EXCHANGE_ID}"
    fun exchangeDetailRoute(id: String): String = "$EXCHANGE_DETAIL/${Uri.encode(id)}"
}

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

/*
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.ExchangesList.route) {

        //Tela de Exchanges
        */
/*composable(Screen.ExchangesList.route) {
            ExchangesScreen { exchange ->
                //Vai para a tela de detalhes
                navController.navigate(Screen.ExchangeDetail.createRoute(exchange.id))
            }
        }*//*


        //Tela de Exchanges
        composable(Screen.ExchangesList.route) {
            ExchangesScreen(navController = navController)
        }

        //Tela de detalhe das exchanges
        composable(
            route = Screen.ExchangeDetail.route + "/{exchangeId}",
            arguments = listOf(navArgument("exchangeId") { type = NavType.StringType })
        ) { backStackEntry ->

            ExchangeDetailScreen(
                viewModel = hiltViewModel(backStackEntry), // importante: amarra ao backStackEntry com o arg
                onBack = { navController.navigateUp() }
            )
        }

    }
}
*/

/*
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ExchangesList.route
    ) {
        composable(route = Routes.ExchangesList.route) {
            ExchangesScreen(
                onExchangeClick = { exchangeId ->
                    navController.navigate(Routes.ExchangeDetail(exchangeId).route)
                }
            )
        }

        composable(
            route = Routes.ExchangeDetail.routePattern,
            arguments = listOf(
                navArgument("exchangeId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val exchangeId = backStackEntry.arguments?.getString("exchangeId") ?: ""
            */
/*ExchangeDetailScreen(
                exchangeId = exchangeId,
                onBackClick = { navController.popBackStack() }
            )*//*


        }
    }
}
*/
