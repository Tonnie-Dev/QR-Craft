package com.tonyxlab.qrcraft.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tonyxlab.qrcraft.navigation.AppNavHost
import com.tonyxlab.qrcraft.navigation.rememberNavOperations
import com.tonyxlab.qrcraft.presentation.theme.QRCraftTheme
import com.tonyxlab.qrcraft.presentation.core.utils.spacing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition { false }
        }
        enableEdgeToEdge()

        setContent {
            QRCraftTheme {

                val padding = MaterialTheme.spacing.spaceDefault
                AppNavHost(
                        navOperations = rememberNavOperations(),
                        modifier = Modifier.padding(all = padding)
                )
            }
        }
    }
}

