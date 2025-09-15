package com.mercadobitcoin.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mercadobitcoin.core.common.NumberFormatter
import com.mercadobitcoin.data.remote.dto.CurrencyDto

@Composable
fun CurrencyChart(
    currencies: List<CurrencyDto>,
    modifier: Modifier = Modifier
) {
    if (currencies.isEmpty()) return

    // Total em USD (pode ser balance * price se tiver quantidade)
    val total = currencies.sumOf { it.priceUsd.toDouble() }

    // Paleta de cores
    val colors = listOf(
        Color(0xFFE8560C), // laranja
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        Color(0xFF4CAF50),
        Color(0xFF03A9F4)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gráfico em pizza
        Canvas(modifier = Modifier.size(120.dp)) {
            var startAngle = 0f
            currencies.forEachIndexed { index, currency ->
                val percentage = (currency.priceUsd.toDouble() / total)
                val sweepAngle = (percentage * 360f).toFloat()

                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(size.width, size.height)
                )

                startAngle += sweepAngle
            }
        }

        Spacer(Modifier.height(8.dp))

        // Legenda com percentual
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            currencies.forEachIndexed { index, currency ->
                val percentage = (currency.priceUsd.toDouble() / total) * 100
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(12.dp)
                            .background(colors[index % colors.size])
                    )

                   Text(
                        text = if (currency.name.length > 15) {
                            "${currency.name.take(15)}" + "…" + " [${"%.0f".format(percentage)}%]: ${
                                NumberFormatter.formatCurrency(
                                    currency.priceUsd.toBigDecimal()
                                )
                            }"
                        } else {
                            "${currency.name} [${"%.0f".format(percentage)}%]: ${
                                NumberFormatter.formatCurrency(
                                    currency.priceUsd.toBigDecimal()
                                )
                            }"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

