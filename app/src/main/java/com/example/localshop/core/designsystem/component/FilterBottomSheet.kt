package com.example.localshop.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.localshop.feature.category.domain.model.Category
import com.example.localshop.feature.product.domain.model.ProductFilters
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentFilters: ProductFilters,
    categories: List<Category> = emptyList(),
    onFiltersApplied: (ProductFilters) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    
    var minPrice by remember { mutableStateOf(currentFilters.minPrice?.toFloat() ?: 0f) }
    var maxPrice by remember { mutableStateOf(currentFilters.maxPrice?.toFloat() ?: 10000f) }
    var selectedCategorySlug by remember { mutableStateOf(currentFilters.categorySlug) }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Filter Products",
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Category Filter
            if (categories.isNotEmpty()) {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                SearchableCategoryDropdown(
                    categories = categories,
                    selectedCategorySlug = selectedCategorySlug,
                    onCategorySelected = { categorySlug ->
                        selectedCategorySlug = categorySlug
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Price Range Filter
            Text(
                text = "Price Range",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = if (minPrice == 0f) "" else String.format("%.0f", minPrice),
                    onValueChange = { value ->
                        minPrice = value.toFloatOrNull() ?: 0f
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Min Price") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = if (maxPrice == 10000f) "" else String.format("%.0f", maxPrice),
                    onValueChange = { value ->
                        maxPrice = value.toFloatOrNull() ?: 10000f
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Max Price") },
                    singleLine = true
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        // Clear all filters
                        val clearedFilters = ProductFilters()
                        onFiltersApplied(clearedFilters)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) onDismiss()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) onDismiss()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val newFilters = currentFilters.copy(
                            categorySlug = selectedCategorySlug,
                            minPrice = if (minPrice > 0) minPrice.toDouble() else null,
                            maxPrice = if (maxPrice < 10000) maxPrice.toDouble() else null
                        )
                        onFiltersApplied(newFilters)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) onDismiss()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}