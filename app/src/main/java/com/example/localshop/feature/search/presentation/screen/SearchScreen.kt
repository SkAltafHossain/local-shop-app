package com.example.localshop.feature.search.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.example.localshop.core.designsystem.component.ProductCard
import com.example.localshop.core.designsystem.component.AppTextField
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.navigation.Screen
import com.example.localshop.feature.search.presentation.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
            .padding(16.dp)
    ) {
        // Search Bar
        AppTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = "Search products",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Search,
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when {
            uiState.isLoading && uiState.searchResults.isEmpty() -> {
                LoadingIndicator(message = "Searching...")
            }
            uiState.errorMessage != null && uiState.searchResults.isEmpty() -> {
                ErrorView(
                    message = uiState.errorMessage ?: "Search failed",
                    onRetry = { viewModel.onSearchQueryChanged(uiState.searchQuery) }
                )
            }
            uiState.searchQuery.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Search for products",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.secondaryText
                    )
                }
            }
            uiState.searchResults.isEmpty() && uiState.searchQuery.isNotEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No results found for \"${uiState.searchQuery}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.secondaryText
                    )
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.searchResults) { product ->
                        ProductCard(
                            name = product.name,
                            price = product.price,
                            discountPrice = product.discountPrice,
                            imageUrl = product.imageUrl,
                            rating = product.rating,
                            reviewCount = product.reviewsCount,
                            inStock = (product.stock ?: 0) > 0,
                            modifier = Modifier.fillMaxWidth(),
                            onProductClick = {
                                navController.navigate(
                                    Screen.ProductDetails.createRoute(product.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
    
    LaunchedEffect(Unit) {
        viewModel.clearSearch()
    }
}