package com.example.localshop.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.localshop.feature.category.domain.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableCategoryDropdown(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val selectedCategory = categories.find { it.id == selectedCategoryId }
    val displayText = selectedCategory?.name ?: "All Categories"

    val filteredCategories = remember(searchQuery, categories) {
        if (searchQuery.isEmpty()) {
            categories
        } else {
            categories.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Row(
        modifier = modifier
            .clickable { expanded = true }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Select category",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    if (expanded) {
        CategorySelectionDialog(
            categories = categories,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            filteredCategories = filteredCategories,
            selectedCategoryId = selectedCategoryId,
            onCategorySelected = { categoryId ->
                onCategorySelected(categoryId)
                expanded = false
                searchQuery = ""
            },
            onDismiss = {
                expanded = false
                searchQuery = ""
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableCategoryDropdown(
    categories: List<Category>,
    selectedCategorySlug: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val selectedCategory = categories.find { it.slug == selectedCategorySlug }
    val displayText = selectedCategory?.name ?: "All Categories"

    val filteredCategories = remember(searchQuery, categories) {
        if (searchQuery.isEmpty()) {
            categories
        } else {
            categories.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Row(
        modifier = modifier
            .clickable { expanded = true }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Select category",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    if (expanded) {
        CategorySelectionDialog(
            categories = categories,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            filteredCategories = filteredCategories,
            selectedCategorySlug = selectedCategorySlug,
            onCategorySelected = { categorySlug ->
                onCategorySelected(categorySlug)
                expanded = false
                searchQuery = ""
            },
            onDismiss = {
                expanded = false
                searchQuery = ""
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelectionDialog(
    categories: List<Category>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    filteredCategories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int?) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Category",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search categories...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    // All Categories option
                    item {
                        CategoryListItem(
                            name = "All Categories",
                            isSelected = selectedCategoryId == null,
                            onClick = { onCategorySelected(null) }
                        )
                    }

                    // Filtered categories
                    items(filteredCategories) { category ->
                        CategoryListItem(
                            name = category.name,
                            isSelected = selectedCategoryId == category.id,
                            onClick = { onCategorySelected(category.id) }
                        )
                    }

                    if (filteredCategories.isEmpty() && searchQuery.isNotEmpty()) {
                        item {
                            Text(
                                text = "No categories found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelectionDialog(
    categories: List<Category>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    filteredCategories: List<Category>,
    selectedCategorySlug: String?,
    onCategorySelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Category",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search categories...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    // All Categories option
                    item {
                        CategoryListItem(
                            name = "All Categories",
                            isSelected = selectedCategorySlug == null,
                            onClick = { onCategorySelected(null) }
                        )
                    }

                    // Filtered categories
                    items(filteredCategories) { category ->
                        CategoryListItem(
                            name = category.name,
                            isSelected = selectedCategorySlug == category.slug,
                            onClick = { onCategorySelected(category.slug) }
                        )
                    }

                    if (filteredCategories.isEmpty() && searchQuery.isNotEmpty()) {
                        item {
                            Text(
                                text = "No categories found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryListItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}