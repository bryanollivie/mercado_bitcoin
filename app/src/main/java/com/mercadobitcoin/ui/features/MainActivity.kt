package com.mercadobitcoin.ui.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.mercadobitcoin.ui.navigation.AppNavGraph
import com.mercadobitcoin.ui.theme.MercadoBitcoinTheme
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
    // Criado UMA vez; escopo raiz estável
    val navController = rememberNavController()

    MaterialTheme {
        AppNavGraph(navController = navController)
    }
}

/*
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ac)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setContent {
            MercadoBitcoinTheme  {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph()
                }
            }
        }
    }
}*/
