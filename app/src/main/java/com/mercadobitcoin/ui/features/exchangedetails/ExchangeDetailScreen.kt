package com.mercadobitcoin.ui.features.exchangedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mercadobitcoin.domain.model.Exchange

@Composable
fun ExchangeDetailScreen(exchange: Exchange) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        AsyncImage (
            model = exchange.logoUrl,
            contentDescription = "Logo de ${exchange.name}",
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(exchange.name, style = MaterialTheme.typography.headlineMedium)
        Text("Volume: ${exchange.spotVolumeUsd ?: "-"}")
        Text("Data de lançamento: ${exchange.dateLaunched ?: "-"}")
    }
}
