package com.example.localshop.feature.orders.presentation.screen

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.localshop.core.designsystem.component.ErrorModal
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.navigation.Screen
import com.example.localshop.feature.orders.domain.model.Order
import com.example.localshop.feature.orders.domain.model.OrderStatus
import com.example.localshop.feature.orders.presentation.viewmodel.OrderHistoryViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderHistoryScreen(
    navController: NavController,
    viewModel: OrderHistoryViewModel = hiltViewModel()
) {
    val colors = AppTheme.colors
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isConfirmingDelivery by viewModel.isConfirmingDelivery.collectAsState()
    val isDownloadingBill by viewModel.isDownloadingBill.collectAsState()
    val billData by viewModel.billData.collectAsState()
    val billFilePath by viewModel.billFilePath.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(billData) {
        billData?.let { (body, orderId) ->
            Log.d("OrderHistoryScreen", "BillData received for order ID: $orderId")
            try {
                val fileName = "bill_order_$orderId.pdf"
                Log.d("OrderHistoryScreen", "Starting file save for: $fileName")
                val filePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+ use MediaStore
                    Log.d("OrderHistoryScreen", "Using MediaStore (Android 10+)")
                    val resolver = context.contentResolver
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }
                    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    Log.d("OrderHistoryScreen", "MediaStore URI: $uri")
                    uri?.let {
                        resolver.openOutputStream(it)?.use { output ->
                            body.byteStream().copyTo(output)
                        }
                        it.toString()
                    } ?: run {
                        Log.e("OrderHistoryScreen", "Failed to insert into MediaStore")
                        null
                    }
                } else {
                    // Android 9 and below use direct file access
                    Log.d("OrderHistoryScreen", "Using direct file access (Android 9-)")
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsDir, fileName)
                    file.outputStream().use { output ->
                        body.byteStream().copyTo(output)
                    }
                    file.absolutePath
                }
                Log.d("OrderHistoryScreen", "File saved at path: $filePath")
                if (filePath != null) {
                    Log.d("OrderHistoryScreen", "Calling setBillFilePath with: $filePath")
                    viewModel.setBillFilePath(filePath)
                } else {
                    Log.e("OrderHistoryScreen", "filePath is null, cannot call setBillFilePath")
                }
                // Only clear billData, not billFilePath
                viewModel.clearBillDataOnly()
            } catch (e: Exception) {
                Log.e("OrderHistoryScreen", "Error saving bill: ${e.message}", e)
                viewModel.clearBillData()
            }
        } ?: run {
            Log.d("OrderHistoryScreen", "billData is null in LaunchedEffect")
        }
    }

    LaunchedEffect(billFilePath) {
        Log.d("OrderHistoryScreen", "LaunchedEffect for billFilePath triggered. Value: $billFilePath")
        billFilePath?.let { path ->
            Log.d("OrderHistoryScreen", "BillFilePath received: $path")
            Log.d("OrderHistoryScreen", "Showing toast: Download successful")
            Toast.makeText(context, "Download successful", Toast.LENGTH_SHORT).show()
            // Automatically open the file after download
            try {
                val uri = if (path.startsWith("content://")) {
                    Uri.parse(path)
                } else {
                    androidx.core.content.FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        File(path)
                    )
                }
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(Intent.createChooser(intent, "Open PDF"))
                Log.d("OrderHistoryScreen", "Intent started to open PDF")
            } catch (e: Exception) {
                Log.e("OrderHistoryScreen", "Error opening PDF: ${e.message}", e)
                // Handle error silently
            }
            viewModel.clearBillData()
        } ?: run {
            Log.d("OrderHistoryScreen", "billFilePath is null in LaunchedEffect")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.primary)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Orders",
                style = MaterialTheme.typography.titleLarge,
                color = androidx.compose.ui.graphics.Color.White,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { viewModel.loadOrders() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }
        }

        when {
            isLoading && orders.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
            orders.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "No Orders",
                            modifier = Modifier.size(80.dp),
                            tint = colors.secondaryText.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No orders yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.secondaryText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start shopping to see your orders here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.secondaryText.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders) { order ->
                        OrderCard(
                            order = order,
                            colors = colors,
                            onClick = {
                                navController.navigate(Screen.OrderDetails.createRoute(order.id))
                            },
                            onConfirmDelivered = { orderId ->
                                viewModel.confirmDelivered(orderId)
                            },
                            onDownloadBill = { orderId ->
                                viewModel.downloadBill(orderId)
                            },
                            isConfirmingDelivery = isConfirmingDelivery,
                            isDownloadingBill = isDownloadingBill
                        )
                    }
                }
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
private fun OrderCard(
    order: Order,
    colors: com.example.localshop.core.designsystem.theme.AppColors,
    onClick: () -> Unit,
    onConfirmDelivered: (Int) -> Unit,
    onDownloadBill: (Int) -> Unit,
    isConfirmingDelivery: Boolean,
    isDownloadingBill: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Order ID and Status
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

            Spacer(modifier = Modifier.height(12.dp))

            // Product preview (first item)
            if (order.items.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = order.items[0].productImage,
                        contentDescription = order.items[0].productName,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(4.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = order.items[0].productName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.primaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Qty: ${order.items[0].quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.secondaryText
                        )
                        if (order.items.size > 1) {
                            Text(
                                text = "+${order.items.size - 1} more items",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.secondaryText
                )
                Text(
                    text = "₹${String.format("%.2f", order.totalAmount)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.primaryText,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Download Bill button
                Button(
                    onClick = { onDownloadBill(order.id) },
                    modifier = Modifier.weight(1f),
                    enabled = !isDownloadingBill,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    )
                ) {
                    if (isDownloadingBill) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download Bill",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Bill", style = MaterialTheme.typography.bodySmall)
                    }
                }

                // Confirm Delivered button
                Button(
                    onClick = { onConfirmDelivered(order.id) },
                    modifier = Modifier.weight(1f),
                    enabled = !isConfirmingDelivery,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    if (isConfirmingDelivery) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Confirm Delivered",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Confirm", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
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
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}