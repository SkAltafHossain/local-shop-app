package com.example.localshop.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.localshop.core.designsystem.theme.AppTheme

@Composable
fun ErrorModal(
    errorMessage: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Error Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    colors.error,
                                    colors.error.copy(alpha = 0.7f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = colors.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error Title
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.primaryText,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Error Message
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.secondaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Dismiss Button
                AppGradientButton(
                    text = "OK",
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )
            }
        }
    }
}

@Composable
fun ConfirmationModal(
    orderNumber: String? = null,
    totalAmount: Double,
    itemCount: Int,
    address: String,
    paymentMethod: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Title
                Text(
                    text = "Confirm Order",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.primaryText,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Order Summary
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    OrderSummaryRow(
                        label = "Items",
                        value = "$itemCount item${if (itemCount > 1) "s" else ""}",
                        colors = colors
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OrderSummaryRow(
                        label = "Total Amount",
                        value = "₹${String.format("%.2f", totalAmount)}",
                        colors = colors,
                        isHighlight = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider(color = colors.divider)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Delivery Address",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.secondaryText,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = address,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.primaryText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider(color = colors.divider)

                    Spacer(modifier = Modifier.height(8.dp))

                    OrderSummaryRow(
                        label = "Payment Method",
                        value = paymentMethod.uppercase(),
                        colors = colors
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppSecondaryButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )

                    AppButton(
                        text = "Confirm",
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderSummaryRow(
    label: String,
    value: String,
    colors: com.example.localshop.core.designsystem.theme.AppColors,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isHighlight) colors.primary else colors.primaryText,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
    }
}
