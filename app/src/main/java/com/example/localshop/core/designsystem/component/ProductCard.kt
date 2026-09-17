package com.example.localshop.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ProductCard(
    name: String,
    price: Double,
    discountPrice: Double? = null,
    imageUrl: String? = null,
    isInWishlist: Boolean = false,
    rating: Double? = null,
    reviewCount: Int? = null,
    inStock: Boolean = true,
    modifier: Modifier = Modifier,
    onProductClick: () -> Unit = {},
    onWishlistClick: (() -> Unit)? = null,
    onAddToCartClick: (() -> Unit)? = null,
    onBuyNowClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image section with badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
                
                // Heart icon in top left
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    IconButton(
                        onClick = { onWishlistClick?.invoke() },
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Icon(
                            imageVector = if (isInWishlist) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = if (isInWishlist) "Remove from wishlist" else "Add to wishlist",
                            tint = if (isInWishlist) {
                                Color(0xFFFF6B6B)
                            } else {
                                Color.Gray
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                
                // Discount badge in top right
                if (discountPrice != null && discountPrice < price) {
                    val discountPercentage = ((price - discountPrice) / price * 100).toInt()
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                color = Color(0xFFFF6B6B),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${discountPercentage}% OFF",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }
            
            // Content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Brand label
                Text(
                    text = "Test",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                // Product name
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Rating
                if (rating != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format("%.1f", rating),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFFA500),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (reviewCount != null && reviewCount > 0) {
                            Text(
                                text = " ($reviewCount)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Price
                PriceText(
                    price = price,
                    discountPrice = discountPrice
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Stock status
                Text(
                    text = if (inStock) "In Stock" else "Out of Stock",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (inStock) Color(0xFF4CAF50) else Color(0xFFF44336),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Add to Cart button (Orange)
                    androidx.compose.material3.Button(
                        onClick = { onAddToCartClick?.invoke() },
                        modifier = Modifier.weight(1f),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF9800),
                            contentColor = Color.White
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 8.dp,
                            vertical = 6.dp
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "ADD TO CART",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Buy Now button (Blue)
                    androidx.compose.material3.Button(
                        onClick = { onBuyNowClick?.invoke() },
                        modifier = Modifier.weight(1f),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            contentColor = Color.White
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 8.dp,
                            vertical = 6.dp
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "BUY NOW",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}