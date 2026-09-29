package com.example.localshop.feature.checkout.presentation.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.localshop.feature.checkout.presentation.viewmodel.CheckoutViewModel

@Composable
fun CheckoutScreen(
    navController: NavController,
    productId: String = "",
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors

    // Load data based on whether we're in Buy Now mode or regular checkout
    LaunchedEffect(productId) {
        if (productId.isNotEmpty()) {
            // Buy Now mode - load single product
            viewModel.loadBuyNowProduct(productId.toIntOrNull() ?: 0)
        } else {
            // Regular checkout - load cart
            viewModel.loadCart()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
    ) {
        when {
            uiState.isLoading && uiState.cart == null -> {
                LoadingIndicator(message = "Loading checkout...")
            }
            uiState.errorMessage != null && uiState.cart == null -> {
                ErrorView(
                    message = uiState.errorMessage ?: "Error loading checkout",
                    onRetry = {
                        viewModel.clearErrorMessage()
                        if (uiState.isBuyNowMode) {
                            viewModel.loadBuyNowProduct(productId.toIntOrNull() ?: 0)
                        } else {
                            viewModel.loadCart()
                        }
                    }
                )
            }
            uiState.cart?.items?.isEmpty() == true -> {
                EmptyState(
                    message = "Your cart is empty",
                    actionText = "Continue Shopping",
                    onAction = { navController.navigate(Screen.Home.route) }
                )
            }
            uiState.checkoutResponse != null -> {
                uiState.checkoutResponse?.let { checkoutResponse ->
                    OrderSuccessScreen(
                        orderNumber = checkoutResponse.orderNumber,
                        totalAmount = checkoutResponse.totalAmount,
                        onContinueShopping = {
                            viewModel.resetCheckout()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        },
                        onViewOrders = {
                            viewModel.resetCheckout()
                            navController.navigate(Screen.OrderHistory.route) {
                                popUpTo(Screen.Home.route)
                            }
                        },
                        colors = colors
                    )
                }
            }
            else -> {
                val cart = uiState.cart
                if (cart != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (uiState.isBuyNowMode) "Buy Now" else "Checkout",
                                style = MaterialTheme.typography.headlineSmall,
                                color = colors.primaryText,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Address Section
                        AddressSection(
                            onAddAddress = { navController.navigate(Screen.AddressManagement.route) },
                            colors = colors
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Order Summary
                        OrderSummarySection(
                            cart = cart,
                            isBuyNowMode = uiState.isBuyNowMode,
                            colors = colors
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Payment Method Section
                        PaymentMethodSection(
                            selectedMethod = uiState.selectedPaymentMethod,
                            onMethodSelect = { viewModel.selectPaymentMethod(it) },
                            colors = colors
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        

                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Price Details
                        PriceDetailsSection(
                            cart = cart,
                            isBuyNowMode = uiState.isBuyNowMode,
                            colors = colors
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Place Order Button
                        Button(
                            onClick = { viewModel.processCheckout() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            enabled = !uiState.isProcessingCheckout,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primaryButton,
                                contentColor = colors.primaryText
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (uiState.isProcessingCheckout) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = colors.primaryText
                                )
                            } else {
                                Text(
                                    text = if (uiState.isBuyNowMode) "BUY NOW" else "PLACE ORDER",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (uiState.isBuyNowMode) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                enabled = !uiState.isProcessingCheckout,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.secondaryButton,
                                    contentColor = colors.primaryText
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "CONTINUE SHOPPING",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AddressSection(
    onAddAddress: () -> Unit,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Delivery Address",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.primaryText,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = colors.divider,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onAddAddress() }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "+ Add Delivery Address",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun OrderSummarySection(
    cart: com.example.localshop.feature.cart.domain.model.Cart,
    isBuyNowMode: Boolean = false,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = if (isBuyNowMode) "Product Details" else "Order Summary (${cart.items.size} items)",
                style = MaterialTheme.typography.titleMedium,
                color = colors.primaryText,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // In Buy Now mode, show only the single item with full details
            // In Cart mode, show up to 3 items
            val itemsToShow = if (isBuyNowMode) cart.items else cart.items.take(3)

            itemsToShow.forEach { cartItem ->
                OrderSummaryItem(
                    cartItem = cartItem,
                    colors = colors
                )
                if (cartItem != itemsToShow.last()) {
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = colors.divider
                    )
                }
            }

            if (!isBuyNowMode && cart.items.size > 3) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "+ ${cart.items.size - 3} more items",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun OrderSummaryItem(
    cartItem: com.example.localshop.feature.cart.domain.model.CartItem,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = cartItem.productImage,
            contentDescription = cartItem.productName,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cartItem.productName,
                style = MaterialTheme.typography.bodySmall,
                color = colors.primaryText,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            PriceText(
                price = cartItem.price,
                discountPrice = cartItem.discountPrice,
                fontSize = 12
            )
        }

        Text(
            text = "Qty: ${cartItem.quantity}",
            style = MaterialTheme.typography.bodySmall,
            color = colors.secondaryText
        )
    }
}

@Composable
fun PaymentMethodSection(
    selectedMethod: String,
    onMethodSelect: (String) -> Unit,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Payment Method",
                style = MaterialTheme.typography.titleMedium,
                color = colors.primaryText,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            PaymentOption(
                title = "Cash on Delivery",
                description = "Pay cash at your doorstep",
                value = "cod",
                selected = selectedMethod == "cod",
                onSelect = onMethodSelect,
                colors = colors
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            PaymentOption(
                title = "UPI",
                description = "Pay using UPI apps",
                value = "upi",
                selected = selectedMethod == "upi",
                onSelect = onMethodSelect,
                colors = colors
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            PaymentOption(
                title = "Credit/Debit Card",
                description = "Pay using your card",
                value = "card",
                selected = selectedMethod == "card",
                onSelect = onMethodSelect,
                colors = colors
            )
        }
    }
}

@Composable
fun PaymentOption(
    title: String,
    description: String,
    value: String,
    selected: Boolean,
    onSelect: (String) -> Unit,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(value) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.primaryText,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = colors.secondaryText
            )
        }
        
        RadioButton(
            selected = selected,
            onClick = { onSelect(value) },
            colors = RadioButtonDefaults.colors(
                selectedColor = colors.primary
            )
        )
    }
}

@Composable
fun PriceDetailsSection(
    cart: com.example.localshop.feature.cart.domain.model.Cart,
    isBuyNowMode: Boolean = false,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                label = "Price (${if (isBuyNowMode) "1 item" else "${cart.items.size} items"})",
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

@Composable
fun OrderSuccessScreen(
    orderNumber: String,
    totalAmount: Double,
    onContinueShopping: () -> Unit,
    onViewOrders: () -> Unit,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color.Green,
            modifier = Modifier.size(80.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Order Placed Successfully!",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.primaryText,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Order #$orderNumber",
            style = MaterialTheme.typography.titleMedium,
            color = colors.secondaryText
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Total Amount: ₹${String.format("%.2f", totalAmount)}",
            style = MaterialTheme.typography.titleLarge,
            color = colors.primary,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onViewOrders,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primaryButton,
                contentColor = colors.primaryText
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "View Orders",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Button(
            onClick = onContinueShopping,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.secondaryButton,
                contentColor = colors.primaryText
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Continue Shopping",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}