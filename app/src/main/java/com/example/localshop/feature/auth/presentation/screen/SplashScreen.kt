package com.example.localshop.feature.auth.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.localshop.core.designsystem.theme.AppTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.localshop.core.designsystem.component.SmallLoadingIndicator
import com.example.localshop.core.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {
    val colors = AppTheme.colors
    
    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds
        // TODO: Check if user is logged in and navigate accordingly
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Local Shop",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        SmallLoadingIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}