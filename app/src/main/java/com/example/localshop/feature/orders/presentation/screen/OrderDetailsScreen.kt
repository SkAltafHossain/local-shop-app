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
import androidx.compose.ui.platform.LocalContext
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
import java.io.File
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
    val isDownloadingBill by viewModel.isDownloadingBill.collectAsState()
    val billData by viewModel.billData.collectAsState()
    val billFilePath by viewModel.billFilePath.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(orderId) {
        if (orderId > 0) {
            viewModel.loadOrderDetails(orderId)
        }
    }

    LaunchedEffect(billData) {
        billData?.let { (body, orderId) ->
            Log.d("OrderDetailsScreen", "BillData received for order ID: $orderId")
            try {
                val fileName = "bill_order_$orderId.pdf"
                Log.d("OrderDetailsScreen", "Starting file save for: $fileName")
                val filePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+ use MediaStore
                    Log.d("OrderDetailsScreen", "Using MediaStore (Android 10+)")
                    val resolver = context.contentResolver
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }
                    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    Log.d("OrderDetailsScreen", "MediaStore URI: $uri")
                    uri?.let {
                        resolver.openOutputStream(it)?.use { output ->
                            body.byteStream().copyTo(output)
                        }
                        it.toString()
                    } ?: run {
                        Log.e("OrderDetailsScreen", "Failed to insert into MediaStore")
                        null
                    }
                } else {
                    // Android 9 and below use direct file access
                    Log.d("OrderDetailsScreen", "Using direct file access (Android 9-)")
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsDir, fileName)
                    file.outputStream().use { output ->
                        body.byteStream().copyTo(output)
                    }
                    file.absolutePath
                }
                Log.d("OrderDetailsScreen", "File saved at path: $filePath")
                if (filePath != null) {
                    Log.d("OrderDetailsScreen", "Calling setBillFilePath with: $filePath")
                    viewModel.setBillFilePath(filePath)
                } else {
                    Log.e("OrderDetailsScreen", "filePath is null, cannot call setBillFilePath")
                }
                // Only clear billData, not billFilePath
                viewModel.clearBillDataOnly()
            } catch (e: Exception) {
                Log.e("OrderDetailsScreen", "Error saving bill: ${e.message}", e)
                viewModel.clearBillData()
            }
        } ?: run {
            Log.d("OrderDetailsScreen", "billData is null in LaunchedEffect")
        }
    }

    LaunchedEffect(billFilePath) {
        Log.d("OrderDetailsScreen", "LaunchedEffect for billFilePath triggered. Value: $billFilePath")
        billFilePath?.let { path ->
            Log.d("OrderDetailsScreen", "BillFilePath received: $path")
            Log.d("OrderDetailsScreen", "Showing toast: Download successful")
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
                Log.d("OrderDetailsScreen", "Intent started to open PDF")
            } catch (e: Exception) {
                Log.e("OrderDetailsScreen", "Error opening PDF: ${e.message}", e)
                // Handle error silently
            }
            viewModel.clearBillData()
        } ?: run {
            Log.d("OrderDetailsScreen", "billFilePath is null in LaunchedEffect")
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
                    val currentOrder = order!!
                    OrderDetailsContent(
                        order = currentOrder,
                        colors = colors,
                        modifier = Modifier.fillMaxSize(),
                        isDownloadingBill = isDownloadingBill,
                        onDownloadBill = { viewModel.downloadBill(currentOrder.id) }
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
    modifier: Modifier = Modifier,
    isDownloadingBill: Boolean = false,
    onDownloadBill: () -> Unit = {}
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

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppOutlinedButton(
                text = if (isDownloadingBill) "Downloading..." else "Download Bill",
                onClick = onDownloadBill,
                enabled = !isDownloadingBill,
                modifier = Modifier.weight(1f)
            )
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