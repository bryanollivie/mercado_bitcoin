package com.mercadobitcoin.ui

sealed class Routes(val route: String) {
    object ExchangesList : Routes("exchanges_list")
    data class ExchangeDetail(val exchangeId: String) : Routes("exchange_detail/$exchangeId") {
        companion object {
            const val routePattern = "exchange_detail/{exchangeId}"
        }
    }
}