package com.example.localshop.feature.category.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.localshop.core.designsystem.component.ErrorView
import com.example.localshop.core.designsystem.component.LoadingIndicator
import com.example.localshop.core.designsystem.component.ProductCard
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.navigation.Screen
import com.example.localshop.feature.category.presentation.viewmodel.CategoryDetailsViewModel

@Composable
fun CategoryDetailsScreen(
    navController: NavController,
    categoryId: String,
    viewModel: CategoryDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors
    
    LaunchedEffect(categoryId) {
        viewModel.loadCategoryDetails(categoryId)
    }
    
    when {
        uiState.isLoading && uiState.categoryWithProducts == null -> {
            LoadingIndicator(message = "Loading category details...")
        }
        uiState.errorMessage != null && uiState.categoryWithProducts == null -> {
            ErrorView(
                message = uiState.errorMessage ?: "",
                onRetry = { viewModel.refresh() }
            )
        }
        else -> {
            val categoryWithProducts = uiState.categoryWithProducts
            if (categoryWithProducts != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.pageBackground)
                        .padding(16.dp)
                ) {
                    // Category Header
                    AsyncImage(
                        model = categoryWithProducts.category.imageUrl,
                        contentDescription = categoryWithProducts.category.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = categoryWithProducts.category.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.primaryText
                    )
                    
                    if (categoryWithProducts.category.description != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = categoryWithProducts.category.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.secondaryText
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Products (${categoryWithProducts.products.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.primaryText
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Products Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(categoryWithProducts.products) { product ->
                            ProductCard(
                                name = product.name,
                                price = product.price,
                                discountPrice = product.discountPrice,
                                imageUrl = product.imageUrl,
                                rating = product.rating,
                                reviewCount = product.reviewsCount,
                                inStock = (product.stock ?: 0) > 0,
                                modifier = Modifier.width(160.dp),
                                onProductClick = {
                                    navController.navigate(Screen.ProductDetails.createRoute(product.id))
                                }
                            )
                        }
                    }
                }
            } else {
                ErrorView(
                    message = "No category data available",
                    onRetry = { viewModel.refresh() }
                )
            }
        }
    }
}