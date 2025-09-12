package com.mercadobitcoin.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
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

            CmcExchangeTheme {
                NavigationGraph(navController = navController)
            }
        }
    }

    @Test
    fun navigation_startsAtExchangesList() {
        // Then
        assert(navController.currentBackStackEntry?.destination?.route == "exchanges_list")
    }

    @Test
    fun navigation_clickExchange_navigatesToDetail() {
        // When
        composeTestRule
            .onNodeWithText("Binance")
            .performClick()

        // Then
        assert(navController.currentBackStackEntry?.destination?.route?.startsWith("exchange_detail/") == true)
    }

    @Test
    fun navigation_backFromDetail_returnsToList() {
        // Given - Navigate to detail
        navController.navigate("exchange_detail/1")

        // When - Click back
        composeTestRule
            .onNodeWithContentDescription("Navigate back")
            .performClick()

        // Then
        assert(navController.currentBackStackEntry?.destination?.route == "exchanges_list")
    }
}