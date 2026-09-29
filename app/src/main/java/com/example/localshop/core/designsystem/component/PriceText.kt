package com.example.localshop.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localshop.core.designsystem.theme.AppTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PriceText(
    price: Double,
    discountPrice: Double? = null,
    modifier: Modifier = Modifier,
    fontSize: Int = 16
) {
    val colors = AppTheme.colors
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    currencyFormat.maximumFractionDigits = 0
    
    if (discountPrice != null && discountPrice < price) {
        Row(
            modifier = modifier,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = currencyFormat.format(discountPrice),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = colors.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = currencyFormat.format(price),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = (fontSize - 2).sp
                ),
                color = colors.secondaryText,
                textDecoration = TextDecoration.LineThrough
            )
        }
    } else {
        Text(
            text = currencyFormat.format(price),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            ),
            color = colors.primary,
            modifier = modifier
        )
    }
}