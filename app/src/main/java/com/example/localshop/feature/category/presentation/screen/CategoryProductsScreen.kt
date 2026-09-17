package com.example.localshop.feature.category.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.localshop.feature.product.domain.model.ProductFilters
import com.example.localshop.feature.product.presentation.screen.ProductScreen
import com.example.localshop.feature.product.presentation.viewmodel.ProductViewModel

@Composable
fun CategoryProductsScreen(
    navController: NavController,
    categoryId: String,
    productViewModel: ProductViewModel = hiltViewModel()
) {
    var currentFilters by remember { mutableStateOf<ProductFilters>(ProductFilters(categorySlug = categoryId)) }

    LaunchedEffect(categoryId) {
        currentFilters = ProductFilters(categorySlug = categoryId)
        productViewModel.updateFilters(currentFilters)
    }

    ProductScreen(
        navController = navController,
        productViewModel = productViewModel,
        skipInitialLoad = true,
        initialCategorySlug = categoryId
    )
}