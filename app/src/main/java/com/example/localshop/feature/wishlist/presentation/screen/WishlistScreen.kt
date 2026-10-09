package com.example.localshop.feature.wishlist.presentation.screen

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.localshop.core.designsystem.component.EmptyState
import com.example.localshop.core.designsystem.component.ErrorView
import com.example.localshop.core.designsystem.component.LoadingIndicator
import com.example.localshop.core.designsystem.component.PriceText
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.navigation.Screen
import com.example.localshop.feature.wishlist.presentation.viewmodel.WishlistViewModel

@Composable
fun WishlistScreen(
    navController: NavController,
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = com.example.localshop.feature.wishlist.presentation.state.WishlistUiState())
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
    val colors = AppTheme.colors

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Home.route) { inclusive = false }
            }
        } else {
            viewModel.loadWishlist()
        }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && isLoggedIn) {
                viewModel.loadWishlist()
            }
        }

        navController.currentBackStackEntry?.lifecycle?.addObserver(observer)

        onDispose {
            navController.currentBackStackEntry?.lifecycle?.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
    ) {
        when {
            uiState.isLoading && uiState.wishlist == null -> {
                LoadingIndicator(message = "Loading wishlist...")
            }
            uiState.wishlist?.items?.isEmpty() == true -> {
                EmptyState(
                    message = "Your wishlist is empty",
                    actionText = "Continue Shopping",
                    onAction = { navController.navigate(Screen.Home.route) }
                )
            }
            uiState.errorMessage != null -> {
                val errorMessage = uiState.errorMessage ?: "An error occurred"
                ErrorView(
                    message = errorMessage,
                    onRetry = { viewModel.loadWishlist() }
                )
            }
            else -> {
                WishlistContent(
                    uiState = uiState,
                    colors = colors,
                    navController = navController,
                    onRemoveFromWishlist = { viewModel.removeFromWishlist(it) },
                    onClearMessage = { viewModel.clearRemoveFromWishlistMessage() }
                )
            }
        }
    }
}

@Composable
private fun WishlistContent(
    uiState: com.example.localshop.feature.wishlist.presentation.state.WishlistUiState,
    colors: com.example.localshop.core.designsystem.theme.AppColors,
    navController: NavController,
    onRemoveFromWishlist: (Int) -> Unit,
    onClearMessage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "My Wishlist",
                style = MaterialTheme.typography.headlineMedium,
                color = colors.primaryText,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.wishlist?.items ?: emptyList()) { item ->
                WishlistItemCard(
                    item = item,
                    colors = colors,
                    navController = navController,
                    onRemoveFromWishlist = { onRemoveFromWishlist(item.id) },
                    isRemoving = uiState.isRemovingFromWishlist
                )
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun WishlistItemCard(
    item: com.example.localshop.feature.wishlist.domain.model.WishlistItem,
    colors: com.example.localshop.core.designsystem.theme.AppColors,
    navController: NavController,
    onRemoveFromWishlist: () -> Unit,
    isRemoving: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.ProductDetails.createRoute(item.productId))
            },
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
            AsyncImage(
                model = item.product.imageUrl,
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.primaryText,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                if (item.product.category != null) {
                    Text(
                        text = item.product.category.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.secondaryText
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                PriceText(
                    price = item.product.price,
                    discountPrice = item.product.discountPrice,
                    fontSize = 16
                )
            }

            IconButton(
                onClick = onRemoveFromWishlist,
                enabled = !isRemoving
            ) {
                if (isRemoving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove from wishlist",
                        tint = colors.error
                    )
                }
            }
        }
    }
}
