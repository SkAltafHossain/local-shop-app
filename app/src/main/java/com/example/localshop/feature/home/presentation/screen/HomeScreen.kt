package com.example.localshop.feature.home.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.localshop.core.designsystem.component.CategoryCard
import com.example.localshop.core.designsystem.component.ErrorView
import com.example.localshop.core.designsystem.component.LoadingIndicator
import com.example.localshop.core.designsystem.component.ProductCard
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.navigation.Screen
import com.example.localshop.feature.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors
    
    when {
        uiState.isLoading && uiState.shopSettings == null -> {
            LoadingIndicator(message = "Loading home...")
        }
        uiState.errorMessage != null && uiState.shopSettings == null -> {
            ErrorView(
                message = uiState.errorMessage ?: "",
                onRetry = { viewModel.refresh() }
            )
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.pageBackground)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Categories Section
                if (uiState.categories.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Categories",
                                style = MaterialTheme.typography.titleLarge,
                                color = colors.primaryText,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "See All",
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    navController.navigate(Screen.Categories.route)
                                }
                            )
                        }
                    }
                    
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(uiState.categories) { category ->
                                CategoryCard(
                                    name = category.name,
                                    imageUrl = category.imageUrl,
                                    modifier = Modifier.width(100.dp),
                                    onCategoryClick = {
                                        navController.navigate(
                                            Screen.CategoryDetails.createRoute(category.id)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                
                // Products Section
                if (uiState.products.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Products",
                                style = MaterialTheme.typography.titleLarge,
                                color = colors.primaryText,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "See All",
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    navController.navigate(Screen.Products.route)
                                }
                            )
                        }
                    }
                    
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(uiState.products) { product ->
                                ProductCard(
                                    name = product.name,
                                    price = product.price,
                                    discountPrice = product.discountPrice,
                                    imageUrl = product.imageUrl,
                                    rating = product.rating,
                                    reviewCount = product.reviewsCount,
                                    inStock = (product.stock ?: 0) > 0,
                                    modifier = Modifier.width(200.dp),
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
                
                // Latest Products Section
                if (uiState.latestProducts.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Latest Products",
                                style = MaterialTheme.typography.titleLarge,
                                color = colors.primaryText,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "See All",
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    navController.navigate(Screen.LatestProducts.route)
                                }
                            )
                        }
                    }
                    
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(uiState.latestProducts) { product ->
                                ProductCard(
                                    name = product.name,
                                    price = product.price,
                                    discountPrice = product.discountPrice,
                                    imageUrl = product.imageUrl,
                                    rating = product.rating,
                                    reviewCount = product.reviewsCount,
                                    inStock = (product.stock ?: 0) > 0,
                                    modifier = Modifier.width(200.dp),
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
                
                // Featured Products Section
                if (uiState.featuredProducts.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Featured Products",
                                style = MaterialTheme.typography.titleLarge,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "See All",
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.secondaryText,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    navController.navigate(Screen.FeaturedProducts.route)
                                }
                            )
                        }
                    }
                    
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(uiState.featuredProducts) { product ->
                                ProductCard(
                                    name = product.name,
                                    price = product.price,
                                    discountPrice = product.discountPrice,
                                    imageUrl = product.imageUrl,
                                    rating = product.rating,
                                    reviewCount = product.reviewsCount,
                                    inStock = (product.stock ?: 0) > 0,
                                    modifier = Modifier.width(200.dp),
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
                
                // Bottom spacer for navigation
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}