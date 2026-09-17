package com.example.localshop.feature.product.presentation.screen

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.localshop.core.designsystem.component.ErrorView
import com.example.localshop.core.designsystem.component.LoadingIndicator
import com.example.localshop.feature.product.presentation.viewmodel.ProductDetailsViewModel

@Composable
fun ProductDetailsScreen(
    navController: NavController,
    productId: String,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors
    
    LaunchedEffect(productId) {
        viewModel.loadProductDetails(productId.toIntOrNull() ?: 0)
    }
    
    when {
        uiState.isLoading && uiState.product == null -> {
            LoadingIndicator(message = "Loading product details...")
        }
        uiState.errorMessage != null && uiState.product == null -> {
            ErrorView(
                message = uiState.errorMessage ?: "",
                onRetry = { viewModel.loadProductDetails(productId.toIntOrNull() ?: 0) }
            )
        }
        uiState.product != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.pageBackground)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.product?.name ?: "Product",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.primaryText
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Price: $${uiState.product?.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.secondaryText
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "TODO: Implement full product details UI",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.otherText
                )
            }
        }
    }
}