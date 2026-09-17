package com.example.localshop.feature.cart.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.localshop.core.designsystem.theme.AppTheme

@Composable
fun CartScreen(
    navController: NavController
) {
    val colors = AppTheme.colors
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cart Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.primaryText
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "TODO: Implement cart functionality",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText
        )
    }
}