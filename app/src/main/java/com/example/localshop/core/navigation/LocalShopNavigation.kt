package com.example.localshop.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.localshop.feature.auth.presentation.screen.SplashScreen
import com.example.localshop.feature.auth.presentation.screen.LoginScreen
import com.example.localshop.feature.auth.presentation.screen.RegisterScreen
import com.example.localshop.feature.auth.presentation.screen.ForgotPasswordScreen
import com.example.localshop.feature.auth.presentation.screen.ResetPasswordScreen
import com.example.localshop.feature.home.presentation.screen.HomeScreen
import com.example.localshop.feature.category.presentation.screen.CategoriesScreen
import com.example.localshop.feature.category.presentation.screen.CategoryDetailsScreen
import com.example.localshop.feature.category.presentation.screen.CategoryProductsScreen
import com.example.localshop.feature.search.presentation.screen.SearchScreen
import com.example.localshop.feature.product.presentation.screen.ProductDetailsScreen
import com.example.localshop.feature.product.presentation.screen.ProductScreen
import com.example.localshop.feature.product.presentation.screen.ProductType
import com.example.localshop.feature.cart.presentation.screen.CartScreen
import com.example.localshop.feature.wishlist.presentation.screen.WishlistScreen
import com.example.localshop.feature.checkout.presentation.screen.CheckoutScreen
import com.example.localshop.feature.address.presentation.screen.AddressManagementScreen
import com.example.localshop.feature.orders.presentation.screen.OrderHistoryScreen
import com.example.localshop.feature.orders.presentation.screen.OrderDetailsScreen
import com.example.localshop.feature.profile.presentation.screen.ProfileScreen
import com.example.localshop.feature.settings.presentation.screen.SettingsScreen

@Composable
fun LocalShopNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash screen
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        
        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
        
        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(navController = navController)
        }
        
        // Main app screens
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Products.route) {
            ProductScreen(navController = navController, productType = ProductType.ALL)
        }
        
        composable(Screen.LatestProducts.route) {
            ProductScreen(navController = navController, productType = ProductType.LATEST)
        }
        
        composable(Screen.FeaturedProducts.route) {
            ProductScreen(navController = navController, productType = ProductType.FEATURED)
        }
        
        composable(Screen.Categories.route) {
            CategoriesScreen(navController = navController)
        }
        
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        
        composable(Screen.ProductDetails.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailsScreen(navController = navController, productId = productId)
        }
        
        composable(Screen.Cart.route) {
            CartScreen(navController = navController)
        }
        
        composable(Screen.Wishlist.route) {
            WishlistScreen(navController = navController)
        }
        
        composable(Screen.Checkout.route) {
            CheckoutScreen(navController = navController)
        }
        
        composable(Screen.AddressManagement.route) {
            AddressManagementScreen(navController = navController)
        }
        
        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(navController = navController)
        }
        
        composable(Screen.OrderDetails.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailsScreen(navController = navController, orderId = orderId)
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        
        composable(Screen.CategoryDetails.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryDetailsScreen(navController = navController, categoryId = categoryId)
        }
        
        composable(Screen.CategoryProducts.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryProductsScreen(navController = navController, categoryId = categoryId)
        }
    }
}