package com.example.localshop.core.navigation

sealed class Screen(val route: String) {
    // Auth screens
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object ForgotPassword : Screen("forgot_password")
    data object ResetPassword : Screen("reset_password")
    
    // Main app screens
    data object Home : Screen("home")
    data object Products : Screen("products")
    data object LatestProducts : Screen("latest_products")
    data object FeaturedProducts : Screen("featured_products")
    data object Categories : Screen("categories")
    data object Search : Screen("search")
    data object ProductDetails : Screen("product_details/{productId}") {
        fun createRoute(productId: Int) = "product_details/$productId"
    }
    data object Cart : Screen("cart")
    data object Wishlist : Screen("wishlist")
    data object Checkout : Screen("checkout")
    data object AddressManagement : Screen("address_management")
    data object OrderHistory : Screen("order_history")
    data object OrderDetails : Screen("order_details/{orderId}") {
        fun createRoute(orderId: Int) = "order_details/$orderId"
    }
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    
    // Category screens
    data object CategoryDetails : Screen("category_details/{categoryId}") {
        fun createRoute(categoryId: Int) = "category_details/$categoryId"
    }
}