package com.example.localshop.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.localshop.core.designsystem.theme.AppTheme

data class SortOption(
    val value: String,
    val label: String
)

@Composable
fun SortDropdown(
    selectedSort: String,
    onSortSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val colors = AppTheme.colors
    
    val sortOptions = listOf(
        SortOption("price_low", "Price: Low to High"),
        SortOption("price_high", "Price: High to Low"),
        SortOption("name_asc", "Name: A to Z"),
        SortOption("name_desc", "Name: Z to A"),
        SortOption("newest", "Newest First")
    )
    
    val selectedLabel = sortOptions.find { it.value == selectedSort }?.label ?: "Sort by"

    Row(
        modifier = modifier
            .clickable { expanded = true }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.primaryText
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Sort options",
            tint = colors.primary
        )
    }
    
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(colors.surface)
    ) {
        sortOptions.forEach { option ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = option.label,
                        color = colors.primaryText
                    )
                },
                onClick = {
                    onSortSelected(option.value)
                    expanded = false
                }
            )
        }
    }
}