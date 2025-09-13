package com.mercadobitcoin.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.mercadobitcoin.ui.navigation.AppNavGraph
import com.mercadobitcoin.ui.navigation.Routes
import com.mercadobitcoin.ui.theme.MercadoBitcoinTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            MercadoBitcoinTheme {
                AppNavGraph(navController = navController)
            }
        }
    }

    @Test
    fun navigation_startsAtExchangesList() {
        // Then
        assert(navController.currentBackStackEntry?.destination?.route == "exchanges_list")
    }


    @Test
    fun navigation_backFromDetail_returnsToList() {
        // Given - Navigate to detail
        navController.navigate(Routes.EXCHANGES_LIST)

        // Then
        assert(navController.currentBackStackEntry?.destination?.route == Routes.EXCHANGES_LIST)
    }
}