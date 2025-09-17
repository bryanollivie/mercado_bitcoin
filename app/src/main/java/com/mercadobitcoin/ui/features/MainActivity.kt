package com.mercadobitcoin.ui.features

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.mercadobitcoin.ui.navigation.AppNavGraph
import com.mercadobitcoin.ui.theme.MercadoBitcoinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Esconde status bar para API < 30
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContent { MercadoBitcoinApp() }
    }
}

@Composable
fun MercadoBitcoinApp() {
    val navController = rememberNavController()
    MercadoBitcoinTheme {
        AppNavGraph(navController = navController)
    }
}
