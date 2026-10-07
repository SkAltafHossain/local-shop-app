package com.example.localshop.feature.orders.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.localshop.core.designsystem.component.AppButton
import com.example.localshop.core.designsystem.component.AppOutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.localshop.core.designsystem.component.ErrorModal
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.feature.orders.domain.model.Order
import com.example.localshop.feature.orders.domain.model.OrderStatus
import com.example.localshop.feature.orders.presentation.viewmodel.OrderDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderDetailsScreen(
    navController: NavController,
    orderId: Int,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {
    val colors = AppTheme.colors
    val order by viewModel.order.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(orderId) {
        if (orderId > 0) {
            viewModel.loadOrderDetails(orderId)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Order Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.primary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.pageBackground)
                .padding(paddingValues)
        ) {
            when {
                isLoading && order == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }
                order == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Order not found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.secondaryText
                        )
                    }
                }
                else -> {
                    OrderDetailsContent(
                        order = order!!,
                        colors = colors,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    if (error != null) {
        ErrorModal(
            errorMessage = error!!,
            onDismiss = { viewModel.clearError() }
        )
    }
}

@Composable
private fun OrderDetailsContent(
    order: Order,
    colors: com.example.localshop.core.designsystem.theme.AppColors,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Order Info Card
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.cardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Order #${order.orderId}",
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.primaryText,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formatDate(order.createdAt),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.secondaryText
                        )
                    }
                    OrderStatusBadge(order.orderStatus, colors)
                }

                Divider(color = colors.divider)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Payment Method",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.secondaryText
                    )
                    Text(
                        text = order.paymentMethod,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.primaryText,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Shipping Address Card
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.cardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Shipping Address",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.primaryText,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.customerName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.primaryText
                )
                Text(
                    text = order.customerPhone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.secondaryText
                )
                Text(
                    text = order.customerAddress,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.secondaryText
                )
            }
        }

        // Order Items Card
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.cardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Order Items (${order.items.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.primaryText,
                    fontWeight = FontWeight.Bold
                )

                order.items.forEach { item ->
                    OrderItemRow(item, colors)
                    if (item != order.items.last()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = colors.divider)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        // Order Summary Card
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.cardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Order Summary",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.primaryText,
                    fontWeight = FontWeight.Bold
                )

                Divider(color = colors.divider)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Subtotal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.secondaryText
                    )
                    val subtotal = order.items.sumOf { it.total }
                    Text(
                        text = "₹${String.format("%.2f", subtotal)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.primaryText
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Shipping",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.secondaryText
                    )
                    Text(
                        text = "FREE",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4CAF50)
                    )
                }

                Divider(color = colors.divider)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.primaryText,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "₹${String.format("%.2f", order.totalAmount)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons for delivered orders
        if (order.orderStatus == OrderStatus.DELIVERED) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppOutlinedButton(
                    text = "Download Bill",
                    onClick = { /* TODO: Implement download bill */ },
                    modifier = Modifier.weight(1f)
                )
                AppButton(
                    text = "Confirm Delivered",
                    onClick = { /* TODO: Implement confirm delivered */ },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun OrderItemRow(
    item: com.example.localshop.feature.orders.domain.model.OrderItem,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.productImage,
            contentDescription = item.productName,
            modifier = Modifier
                .size(80.dp)
                .padding(4.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.productName,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.primaryText,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Qty: ${item.quantity}",
                style = MaterialTheme.typography.bodySmall,
                color = colors.secondaryText
            )
            Text(
                text = "₹${String.format("%.2f", item.productDiscountPrice)}",
                style = MaterialTheme.typography.bodySmall,
                color = colors.primary,
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "₹${String.format("%.2f", item.total)}",
                style = MaterialTheme.typography.titleMedium,
                color = colors.primaryText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun OrderStatusBadge(
    status: OrderStatus,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    val backgroundColor: Color = when (status) {
        OrderStatus.PENDING -> Color(0xFFFFA726)
        OrderStatus.PROCESSING -> Color(0xFF42A5F5)
        OrderStatus.SHIPPED -> Color(0xFF66BB6A)
        OrderStatus.DELIVERED -> Color(0xFF4CAF50)
        OrderStatus.CANCELLED -> Color(0xFFEF5350)
        OrderStatus.REFUNDED -> Color(0xFFAB47BC)
    }

    androidx.compose.material3.Surface(
        shape = RoundedCornerShape(4.dp),
        color = backgroundColor
    ) {
        Text(
            text = status.displayName,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatDate(dateString: String?): String {
    if (dateString == null) return "N/A"
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}