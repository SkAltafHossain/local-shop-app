package com.example.localshop.feature.product.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.localshop.feature.product.presentation.viewmodel.ProductViewModel

enum class ProductType {
    ALL, LATEST, FEATURED
}

@Composable
fun ProductScreen(
    navController: NavController,
    productType: ProductType = ProductType.ALL,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Load products based on type
    LaunchedEffect(productType) {
        when (productType) {
            ProductType.ALL -> viewModel.loadProducts()
            ProductType.LATEST -> viewModel.loadLatestProducts()
            ProductType.FEATURED -> viewModel.loadFeaturedProducts()
        }
    }
    
    val screenTitle = when (productType) {
        ProductType.ALL -> "All Products"
        ProductType.LATEST -> "Latest Products"
        ProductType.FEATURED -> "Featured Products"
    }
    
    when {
        uiState.isLoading && uiState.products.isEmpty() -> {
            LoadingIndicator(message = "Loading products...")
        }
        uiState.errorMessage != null && uiState.products.isEmpty() -> {
            ErrorView(
                message = uiState.errorMessage ?: "",
                onRetry = { 
                    when (productType) {
                        ProductType.ALL -> viewModel.loadProducts()
                        ProductType.LATEST -> viewModel.loadLatestProducts()
                        ProductType.FEATURED -> viewModel.loadFeaturedProducts()
                    }
                }
            )
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = screenTitle,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Total Products: ${uiState.products.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "TODO: Implement product list UI",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
