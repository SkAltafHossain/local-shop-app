package com.example.localshop.feature.category.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.localshop.core.designsystem.component.CategoryCard
import com.example.localshop.core.navigation.Screen
import com.example.localshop.core.designsystem.component.ErrorView
import com.example.localshop.core.designsystem.component.LoadingIndicator
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.feature.category.presentation.viewmodel.CategoriesViewModel

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors
    
    when {
        uiState.isLoading && uiState.categories.isEmpty() -> {
            LoadingIndicator(message = "Loading categories...")
        }
        uiState.errorMessage != null && uiState.categories.isEmpty() -> {
            ErrorView(
                message = uiState.errorMessage ?: "",
                onRetry = { viewModel.refresh() }
            )
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.pageBackground)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.primaryText,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.categories) { category ->
                        CategoryCard(
                            name = category.name,
                            imageUrl = category.imageUrl,
                            onCategoryClick = {
                                navController.navigate(Screen.CategoryDetails.createRoute(category.id))
                            }
                        )
                    }
                }
            }
        }
    }
}