package com.mercadobitcoin.exchanges

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.ui.features.exchanges.ExchangesScreen
import com.mercadobitcoin.ui.features.exchanges.ExchangesUiState
import com.mercadobitcoin.ui.theme.MercadoBitcoinTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

// Fake ViewModel só com o necessário
class FakeExchangesViewModel(initial: ExchangesUiState) {
    private val _uiState = MutableStateFlow(initial)
    val uiState: StateFlow<ExchangesUiState> = _uiState

    fun update(state: ExchangesUiState) {
        _uiState.value = state
    }

    fun refresh() {
        // no-op, você pode contar chamadas se precisar
    }
}

class ExchangesScreenTest {

    private lateinit var navController: TestNavHostController

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: FakeExchangesViewModel

    @Before
    fun setup() {
        viewModel = FakeExchangesViewModel(ExchangesUiState())
    }

    private fun setContent() {
        composeTestRule.setContent {
            MercadoBitcoinTheme {
                ExchangesScreen(
                   navController = navController
                )
            }
        }
    }

    @Test
    fun showsLoading() {
        viewModel.update(ExchangesUiState(isLoading = true))
        setContent()

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun showsList() {
        val exchanges = listOf(
            Exchange("1", "Binance", "logo.png", BigDecimal("1000"), LocalDate.of(2017, 7, 14)),
            Exchange("2", "Coinbase", "logo2.png", BigDecimal("500"), LocalDate.of(2012, 6, 20))
        )
        viewModel.update(ExchangesUiState(exchanges = exchanges))
        setContent()

        composeTestRule.onNodeWithText("Binance").assertIsDisplayed()
        composeTestRule.onNodeWithText("Coinbase").assertIsDisplayed()
    }

    @Test
    fun clickExchange_triggersCallback() {
        val exchanges = listOf(Exchange("1", "Binance", null, BigDecimal("1000"), null))
        viewModel.update(ExchangesUiState(exchanges = exchanges))

        var clickedId: String? = null
        composeTestRule.setContent {
            MercadoBitcoinTheme {
                ExchangesScreen(
                    navController = navController
                )
            }
        }

        composeTestRule.onNodeWithText("Binance").performClick()
        assert(clickedId == "1")
    }

    @Test
    fun showsError() {
        viewModel.update(ExchangesUiState(error = "Network error"))
        setContent()

        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun showsEmptyState() {
        viewModel.update(ExchangesUiState())
        setContent()

        composeTestRule.onNodeWithText("No exchanges available").assertIsDisplayed()
    }
}
