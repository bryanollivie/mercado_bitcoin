package com.mercadobitcoin.ui.features.exchanges

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mercadobitcoin.core.common.DateFormatter
import com.mercadobitcoin.core.common.NumberFormatter
import com.mercadobitcoin.domain.model.Exchange

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExchangeItem(exchange: Exchange, onClick: () -> Unit) {

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() }
            .semantics { contentDescription = "Exchange ${exchange.name}" },
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(exchange.logoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Logo of ${exchange.name}",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exchange.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = "Volume: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = NumberFormatter.formatCurrency(exchange.spotVolumeUsd),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row {
                    Text(
                        text = "Launched: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = DateFormatter.format(exchange.dateLaunched),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

