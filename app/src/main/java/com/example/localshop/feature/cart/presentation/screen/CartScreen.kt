package com.example.localshop.feature.cart.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.localshop.core.designsystem.component.EmptyState
import com.example.localshop.core.designsystem.component.ErrorView
import com.example.localshop.core.designsystem.component.LoadingIndicator
import com.example.localshop.core.designsystem.component.PriceText
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.navigation.Screen
import com.example.localshop.feature.cart.presentation.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors
    
    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
    ) {
        when {
            uiState.isLoading && uiState.cart == null -> {
                LoadingIndicator(message = "Loading cart...")
            }
            uiState.cart?.items?.isEmpty() == true -> {
                EmptyState(
                    message = "Your cart is empty",
                    actionText = "Continue Shopping",
                    onAction = { navController.navigate(Screen.Home.route) }
                )
            }
            uiState.errorMessage != null && uiState.cart == null -> {
                ErrorView(
                    message = uiState.errorMessage ?: "Error loading cart",
                    onRetry = { viewModel.loadCart() }
                )
            }
            else -> {
                val cart = uiState.cart
                if (cart != null) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Cart Items
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(cart.items) { cartItem ->
                                CartItemCard(
                                    cartItem = cartItem,
                                    onQuantityChange = { newQuantity ->
                                        if (newQuantity > 0) {
                                            viewModel.updateCartItem(cartItem.id, newQuantity)
                                        }
                                    },
                                    onDelete = { viewModel.deleteCartItem(cartItem.id) },
                                    isUpdating = uiState.isUpdatingItem,
                                    isDeleting = uiState.isDeletingItem
                                )
                            }
                        }
                        
                        // Price Details and Checkout Button
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colors.cardBackground
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Price Details",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colors.primaryText,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                PriceDetailRow(
                                    label = "Price (${cart.items.size} items)",
                                    value = cart.subtotal,
                                    colors = colors
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                PriceDetailRow(
                                    label = "Discount",
                                    value = cart.subtotal - cart.total,
                                    colors = colors,
                                    isDiscount = true
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                PriceDetailRow(
                                    label = "Delivery Charges",
                                    value = 0.0,
                                    colors = colors,
                                    isFree = true
                                )
                                
                                Divider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = colors.divider
                                )
                                
                                PriceDetailRow(
                                    label = "Total Amount",
                                    value = cart.total,
                                    colors = colors,
                                    isTotal = true
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Button(
                                    onClick = { navController.navigate(Screen.Checkout.route) },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !uiState.isDeletingItem && !uiState.isClearingCart,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colors.primaryButton,
                                        contentColor = colors.primaryText
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "PLACE ORDER",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: com.example.localshop.feature.cart.domain.model.CartItem,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit,
    isUpdating: Boolean,
    isDeleting: Boolean
) {
    val colors = AppTheme.colors
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image
            AsyncImage(
                model = cartItem.productImage,
                contentDescription = cartItem.productName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.primaryText,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                PriceText(
                    price = cartItem.price,
                    discountPrice = cartItem.discountPrice
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Quantity Controls
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = colors.divider,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity - 1) },
                        enabled = cartItem.quantity > 1 && !isUpdating
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = colors.primaryText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    
                    Text(
                        text = if (isUpdating) "..." else cartItem.quantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.primaryText,
                        fontWeight = FontWeight.Medium
                    )
                    
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity + 1) },
                        enabled = !isUpdating
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = colors.primaryText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            
            // Delete Button
            IconButton(
                onClick = onDelete,
                enabled = !isDeleting
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove item",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun PriceDetailRow(
    label: String,
    value: Double,
    colors: com.example.localshop.core.designsystem.theme.AppColors,
    isDiscount: Boolean = false,
    isFree: Boolean = false,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = if (isTotal) colors.primaryText else colors.secondaryText,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        
        val displayValue = if (isFree) "FREE" else "₹${String.format("%.2f", value)}"
        val valueColor = when {
            isDiscount -> Color.Green
            isFree -> Color.Green
            isTotal -> colors.primaryText
            else -> colors.secondaryText
        }
        
        Text(
            text = displayValue,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            textDecoration = if (isDiscount) TextDecoration.LineThrough else null
        )
    }
}