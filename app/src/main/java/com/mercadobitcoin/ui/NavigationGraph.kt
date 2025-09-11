/*
package com.mercadobitcoin.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mercadobitcoin.ui.feat.exchange.ExchangesScreen


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
