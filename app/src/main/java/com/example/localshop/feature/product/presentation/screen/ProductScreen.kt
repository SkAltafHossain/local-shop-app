package com.example.localshop.feature.product.presentation.screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.localshop.core.designsystem.component.ErrorView
import com.example.localshop.core.designsystem.component.FilterBottomSheet
import com.example.localshop.core.designsystem.component.FilterButton
import com.example.localshop.core.designsystem.component.LoadingIndicator
import com.example.localshop.core.designsystem.component.ProductCard
import com.example.localshop.core.designsystem.component.SortDropdown
import com.example.localshop.core.navigation.Screen
import com.example.localshop.feature.category.domain.model.Category
import com.example.localshop.feature.product.domain.model.ProductFilters
import com.example.localshop.feature.product.presentation.viewmodel.ProductViewModel
import com.example.localshop.feature.category.presentation.viewmodel.CategoryViewModel

enum class ProductType {
    ALL, LATEST, FEATURED
}

@Composable
fun ProductScreen(
    navController: NavController,
    productType: ProductType = ProductType.ALL,
    productViewModel: ProductViewModel = hiltViewModel(),
    skipInitialLoad: Boolean = false,
    initialCategorySlug: String? = null
) {
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val uiState by productViewModel.uiState.collectAsState()
    val categoryUiState by categoryViewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()

    // Load categories for filter
    LaunchedEffect(Unit) {
        if (categoryUiState.categories.isEmpty()) {
            categoryViewModel.loadCategories()
        }
    }

    var showFilterSheet by remember { mutableStateOf(false) }
    var currentSort by remember { mutableStateOf<String?>(null) }
    var currentFilters by remember { mutableStateOf<ProductFilters>(
        when (productType) {
            ProductType.LATEST -> ProductFilters(isNew = true)
            ProductType.FEATURED -> ProductFilters(isFeatured = true)
            ProductType.ALL -> ProductFilters(categorySlug = initialCategorySlug)
        }
    ) }
    
    val categories = categoryUiState.categories
    
    // Load products based on type
    LaunchedEffect(productType, skipInitialLoad) {
        if (!skipInitialLoad) {
            when (productType) {
                ProductType.ALL -> productViewModel.loadProducts()
                ProductType.LATEST -> productViewModel.loadLatestProducts()
                ProductType.FEATURED -> productViewModel.loadFeaturedProducts()
            }
        }
    }
    
    // Infinite scroll pagination
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= layoutInfo.totalItemsCount - 3
        }
    }
    
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !uiState.isLoadingMore && uiState.currentPage < uiState.lastPage) {
            productViewModel.loadMoreProducts()
        }
    }
    
    val screenTitle = when (productType) {
        ProductType.ALL -> "All Products"
        ProductType.LATEST -> "Latest Products"
        ProductType.FEATURED -> "Featured Products"
    }
    
    when {
        uiState.isLoading && uiState.products.isEmpty() -> {
            LoadingIndicator(message = "Loading products...")
        }
        uiState.errorMessage != null && uiState.products.isEmpty() -> {
            ErrorView(
                message = uiState.errorMessage ?: "",
                onRetry = { 
                    when (productType) {
                        ProductType.ALL -> productViewModel.loadProducts()
                        ProductType.LATEST -> productViewModel.loadLatestProducts()
                        ProductType.FEATURED -> productViewModel.loadFeaturedProducts()
                    }
                }
            )
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Filter and Sort Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortDropdown(
                        selectedSort = currentSort ?: "",
                        onSortSelected = { sortValue ->
                            currentSort = sortValue
                            // Preserve the original product type flags when sorting
                            val updatedFilters = when (productType) {
                                ProductType.LATEST -> currentFilters.copy(sort = sortValue, isNew = true)
                                ProductType.FEATURED -> currentFilters.copy(sort = sortValue, isFeatured = true)
                                ProductType.ALL -> currentFilters.copy(sort = sortValue)
                            }
                            currentFilters = updatedFilters
                            productViewModel.updateFilters(updatedFilters)
                        }
                    )
                    
                    FilterButton(
                        onClick = { showFilterSheet = true }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Product Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.products) { product ->
                        ProductCard(
                            name = product.name,
                            price = product.price,
                            discountPrice = product.discountPrice,
                            imageUrl = product.imageUrl,
                            rating = product.rating,
                            reviewCount = product.reviewsCount,
                            inStock = (product.stock ?: 0) > 0,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        Screen.ProductDetails.createRoute(product.id)
                                    )
                                }
                        )
                    }
                    
                    if (uiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Filter Bottom Sheet
    if (showFilterSheet) {
        FilterBottomSheet(
            currentFilters = currentFilters,
            categories = categories,
            onFiltersApplied = { newFilters ->
                // Preserve the original product type flags
                val filtersWithFlags = when (productType) {
                    ProductType.LATEST -> newFilters.copy(isNew = true)
                    ProductType.FEATURED -> newFilters.copy(isFeatured = true)
                    ProductType.ALL -> newFilters
                }
                currentFilters = filtersWithFlags
                productViewModel.updateFilters(filtersWithFlags)
            },
            onDismiss = { showFilterSheet = false }
        )
    }
}
