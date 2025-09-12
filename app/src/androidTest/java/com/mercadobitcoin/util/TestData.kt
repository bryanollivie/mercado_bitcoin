package com.mercadobitcoin.util

import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import java.math.BigDecimal
import java.time.LocalDate

object TestData {

    val exchanges = listOf(
        Exchange(
            id = "1",
            name = "Binance",
            logoUrl = "https://logo1.url",
            spotVolumeUsd = BigDecimal("1000000"),
            dateLaunched = LocalDate.of(2017, 7, 14)
        ),
        Exchange(
            id = "2",
            name = "Coinbase",
            logoUrl = "https://logo2.url",
            spotVolumeUsd = BigDecimal("500000"),
            dateLaunched = LocalDate.of(2012, 6, 20)
        )
    )

    val exchangeDetail = ExchangeDetail(
        id = "1",
        name = "Binance",
        logoUrl = "https://logo.url",
        description = "Leading cryptocurrency exchange",
        websiteUrl = "https://binance.com",
        makerFee = BigDecimal("0.10"),
        takerFee = BigDecimal("0.10"),
        dateLaunched = LocalDate.of(2017, 7, 14),
        //currencies = currencies
    )

    val currencies = listOf(
        CurrencyQuote(
            name = "Bitcoin",
            priceUsd = BigDecimal("50000.00")
        ),
        CurrencyQuote(
            name = "Ethereum",
            priceUsd = BigDecimal("3000.00")
        ),
        CurrencyQuote(
            name = "Binance Coin",
            priceUsd = BigDecimal("400.00")
        )
    )
}