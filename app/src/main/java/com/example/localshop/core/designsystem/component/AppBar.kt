package com.example.localshop.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.localshop.core.designsystem.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    isLoggedIn: Boolean = false,
    onCartClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    showSearchIcon: Boolean = true,
    cartItemCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    
    TopAppBar(
        title = {
            Text(
                text = title,
                color = colors.primaryText
            )
        },
        actions = {
            if (showSearchIcon) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = colors.primaryText
                    )
                }
            }
            if (isLoggedIn) {
                IconButton(onClick = onCartClick) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = colors.primaryText
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.assent
        ),
        modifier = modifier
    )
}
