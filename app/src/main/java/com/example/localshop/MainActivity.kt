package com.example.localshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.localshop.core.auth.SessionManager
import com.example.localshop.core.designsystem.component.AppBar
import com.example.localshop.core.designsystem.component.BottomNavigation
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.designsystem.theme.LocalShopTheme
import com.example.localshop.core.navigation.LocalShopNavigation
import com.example.localshop.core.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalShopTheme {
                val colors = AppTheme.colors
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colors.pageBackground
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
                    
                    val bottomNavItems = listOf(
                        Screen.Home.route,
                        Screen.Products.route,
                        Screen.Categories.route,
                        Screen.Profile.route,
                        Screen.Settings.route
                    )
                    
                    val showBottomNav = currentRoute in bottomNavItems
                    val showAppBar = currentRoute != Screen.Splash.route
                    
                    // Get title based on current route
                    val appBarTitle = when (currentRoute) {
                        Screen.Splash.route -> ""
                        Screen.Login.route -> "Login"
                        Screen.Register.route -> "Register"
                        Screen.ForgotPassword.route -> "Forgot Password"
                        Screen.ResetPassword.route -> "Reset Password"
                        Screen.Home.route -> "Local Shop"
                        Screen.Products.route -> "Products"
                        Screen.LatestProducts.route -> "Latest Products"
                        Screen.FeaturedProducts.route -> "Featured Products"
                        Screen.Categories.route -> "Categories"
                        Screen.Search.route -> "Search"
                        Screen.ProductDetails.route -> "Product Details"
                        Screen.Cart.route -> "Cart"
                        Screen.Wishlist.route -> "Wishlist"
                        Screen.Checkout.route -> "Checkout"
                        Screen.AddressManagement.route -> "Address Management"
                        Screen.OrderHistory.route -> "Order History"
                        Screen.OrderDetails.route -> "Order Details"
                        Screen.Profile.route -> "Profile"
                        Screen.Settings.route -> "Settings"
                        Screen.CategoryDetails.route -> "Category Details"
                        Screen.CategoryProducts.route -> "Category Products"
                        else -> "Local Shop"
                    }
                    
                    Scaffold(
                        topBar = {
                            if (showAppBar) {
                                val showSearchIcon = currentRoute != Screen.Search.route
                                AppBar(
                                    title = appBarTitle,
                                    isLoggedIn = isLoggedIn,
                                    onCartClick = {
                                        navController.navigate(Screen.Cart.route)
                                    },
                                    onSearchClick = {
                                        navController.navigate(Screen.Search.route)
                                    },
                                    showSearchIcon = showSearchIcon
                                )
                            }
                        },
                        bottomBar = {
                            if (showBottomNav) {
                                BottomNavigation(
                                    currentRoute = currentRoute ?: Screen.Home.route,
                                    onNavigate = { route ->
                                        navController.navigate(route) {
                                            popUpTo(route) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            LocalShopNavigation(
                                navController = navController,
                                startDestination = Screen.Home.route
                            )
                        }
                    }
                }
            }
        }
    }
}