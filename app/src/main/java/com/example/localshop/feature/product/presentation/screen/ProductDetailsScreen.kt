package com.example.localshop.feature.product.presentation.screen

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.localshop.core.designsystem.component.ErrorView
import com.example.localshop.core.designsystem.component.LoadingIndicator
import com.example.localshop.core.designsystem.component.PriceText
import com.example.localshop.core.designsystem.component.ProductCard
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.navigation.Screen
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
        uiState.isLoading && uiState.productDetails == null -> {
            LoadingIndicator(message = "Loading product details...")
        }
        uiState.errorMessage != null && uiState.productDetails == null -> {
            ErrorView(
                message = uiState.errorMessage ?: "",
                onRetry = { viewModel.refresh() }
            )
        }
        else -> {
            val productDetails = uiState.productDetails
            if (productDetails != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.pageBackground)
                ) {
                    item {
                        // Product Image
                        AsyncImage(
                            model = productDetails.product.imageUrl,
                            contentDescription = productDetails.product.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Product Name
                            Text(
                                text = productDetails.product.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = colors.primaryText,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Category
                            if (productDetails.product.category != null) {
                                Text(
                                    text = productDetails.product.category.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.primary,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            
                            // Price
                            PriceText(
                                price = productDetails.product.price,
                                discountPrice = productDetails.product.discountPrice
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Stock Status
                            val inStock = (productDetails.product.stock ?: 0) > 0
                            Text(
                                text = if (inStock) "In Stock (${productDetails.product.stock})" else "Out of Stock",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (inStock) Color.Green else Color.Red,
                                fontWeight = FontWeight.Medium
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Description
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.primaryText,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            if (productDetails.product.description != null) {
                                Text(
                                    text = stripHtmlTags(productDetails.product.description),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.secondaryText,
                                    textAlign = TextAlign.Start
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { /* TODO: Add to cart */ },
                                    modifier = Modifier.weight(1f),
                                    enabled = inStock,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colors.secondaryButton,
                                        contentColor = colors.primaryText
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (inStock) "Add to Cart" else "Out of Stock",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Button(
                                    onClick = { /* TODO: Buy now */ },
                                    modifier = Modifier.weight(1f),
                                    enabled = inStock,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colors.primaryButton,
                                        contentColor = colors.primaryText
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Buy Now",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Related Products
                            if (productDetails.relatedProducts.isNotEmpty()) {
                                Text(
                                    text = "Related Products",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = colors.primaryText,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                LazyHorizontalGrid(
                                    rows = GridCells.Fixed(1),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(productDetails.relatedProducts) { product ->
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
                        }
                    }
                }
            } else {
                ErrorView(
                    message = "No product data available",
                    onRetry = { viewModel.refresh() }
                )
            }
        }
    }
}

private fun stripHtmlTags(html: String): String {
    return html.replace("<[^>]*>".toRegex(), "")
}