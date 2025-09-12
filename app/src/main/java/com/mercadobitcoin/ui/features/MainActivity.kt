package com.mercadobitcoin.ui.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.mercadobitcoin.ui.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent { MercadoBitcoinApp() }
        }
    }
}

@Composable
fun MercadoBitcoinApp() {
    val navController = rememberNavController()
    MaterialTheme {
        AppNavGraph(navController = navController)
    }
}
