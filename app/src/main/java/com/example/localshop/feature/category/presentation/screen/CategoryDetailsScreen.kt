package com.example.localshop.feature.category.presentation.screen

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
import com.example.localshop.core.designsystem.theme.AppTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CategoryDetailsScreen(
    navController: NavController,
    categoryId: String
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
            text = "Category Details Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.primaryText
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Category ID: $categoryId",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "TODO: Implement category details functionality",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText
        )
    }
}