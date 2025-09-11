
package com.mercadobitcoin.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mercadobitcoin.ui.features.exchanges.ExchangesScreen


sealed class Screen(val route: String) {

    object ExchangesList : Screen("exchanges_list")

    object ExchangeDetail : Screen("exchange_detail/{exchangeId}") {
        fun createRoute(exchangeId: String) = "exchange_detail/$exchangeId"
    }

}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.ExchangesList.route) {

        //Tela de Exchanges
        composable(Screen.ExchangesList.route) {
            ExchangesScreen { exchange ->
                //Vai para a tela de detalhes
                navController.navigate(Screen.ExchangeDetail.createRoute(exchange.id))
            }
        }

        //Tela de detalhe das exchanges
        composable(
            route = Screen.ExchangeDetail.route,
            arguments = listOf(navArgument("exchangeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exchangeId = backStackEntry.arguments?.getString("exchangeId") ?: return@composable
            // aqui você pode buscar detalhe via ViewModel específico
            Text("Detalhe da Exchange: $exchangeId")
        }
    }
}

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
