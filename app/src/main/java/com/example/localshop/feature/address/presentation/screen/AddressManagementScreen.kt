package com.example.localshop.feature.address.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.localshop.core.designsystem.component.ErrorView
import com.example.localshop.core.designsystem.component.LoadingIndicator
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.feature.address.domain.model.Address
import com.example.localshop.feature.address.presentation.viewmodel.AddressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressManagementScreen(
    navController: NavController,
    viewModel: AddressViewModel = hiltViewModel(),
    fromCheckout: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors
    var showAddDialog by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<Address?>(null) }
    var selectedAddressId by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadAddresses()
    }

    // Auto-select default address when coming from checkout
    LaunchedEffect(uiState.addresses, fromCheckout) {
        if (fromCheckout && uiState.addresses.isNotEmpty() && selectedAddressId == null) {
            val defaultAddress = uiState.addresses.find { it.isDefault }
            if (defaultAddress != null) {
                selectedAddressId = defaultAddress.id
            }
        }
    }

    // Close dialog on success
    LaunchedEffect(uiState.addAddressSuccess, uiState.updateAddressSuccess) {
        if (uiState.addAddressSuccess || uiState.updateAddressSuccess) {
            showAddDialog = false
            editingAddress = null
            viewModel.clearSuccessFlags()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (fromCheckout) "Select Address" else "Manage Addresses",
                        color = colors.primaryText,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.cardBackground
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.pageBackground)
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.addresses.isEmpty() -> {
                    LoadingIndicator(message = "Loading addresses...")
                }
                uiState.errorMessage != null && uiState.addresses.isEmpty() -> {
                    ErrorView(
                        message = uiState.errorMessage ?: "Error loading addresses",
                        onRetry = { viewModel.loadAddresses() }
                    )
                }
                uiState.addresses.isEmpty() -> {
                    EmptyAddressesState(
                        onAddAddress = {
                            editingAddress = null
                            showAddDialog = true
                        }
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Add New Address Button (Flipkart style)
                        if (!fromCheckout) {
                            Button(
                                onClick = {
                                    editingAddress = null
                                    showAddDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.cardBackground,
                                    contentColor = colors.primary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    1.dp,
                                    colors.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Add New Address",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            AddressList(
                                addresses = uiState.addresses,
                                selectedAddressId = selectedAddressId,
                                fromCheckout = fromCheckout,
                                onEditAddress = { address ->
                                    editingAddress = address
                                    showAddDialog = true
                                },
                                onDeleteAddress = { address ->
                                    viewModel.deleteAddress(address.id)
                                },
                                onSetDefault = { address ->
                                    viewModel.setDefaultAddress(address.id)
                                },
                                onSelectAddress = { addressId ->
                                    selectedAddressId = addressId
                                },
                                colors = colors
                            )
                        }

                        // Fixed bottom submit button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colors.cardBackground)
                                .padding(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (fromCheckout && selectedAddressId != null) {
                                        navController.previousBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("selectedAddressId", selectedAddressId)
                                    }
                                    navController.popBackStack()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !fromCheckout || selectedAddressId != null,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.primaryButton,
                                    contentColor = colors.primaryText
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (fromCheckout) "Deliver Here" else "Submit",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddEditAddressDialog(
            address = editingAddress,
            onDismiss = {
                showAddDialog = false
                editingAddress = null
                viewModel.clearSuccessFlags()
            },
            onSave = { address ->
                if (editingAddress != null) {
                    viewModel.updateAddress(editingAddress!!.id, address)
                } else {
                    viewModel.addAddress(address)
                }
            },
            viewModel = viewModel,
            colors = colors,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun EmptyAddressesState(
    onAddAddress: () -> Unit
) {
    val colors = AppTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = colors.secondaryText,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Addresses Yet",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.primaryText,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add your delivery address to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddAddress,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primaryButton,
                contentColor = colors.primaryText
            )
        ) {
            Text("Add Address")
        }
    }
}

@Composable
fun AddressList(
    addresses: List<Address>,
    selectedAddressId: Int? = null,
    fromCheckout: Boolean = false,
    onEditAddress: (Address) -> Unit,
    onDeleteAddress: (Address) -> Unit,
    onSetDefault: (Address) -> Unit,
    onSelectAddress: (Int) -> Unit = {},
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(addresses) { address ->
            AddressCard(
                address = address,
                selected = address.id == selectedAddressId,
                fromCheckout = fromCheckout,
                onEdit = { onEditAddress(address) },
                onDelete = { onDeleteAddress(address) },
                onSetDefault = { onSetDefault(address) },
                onSelect = { onSelectAddress(address.id) },
                colors = colors
            )
        }

    }
}

@Composable
fun AddressCard(
    address: Address,
    selected: Boolean = false,
    fromCheckout: Boolean = false,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit,
    onSelect: () -> Unit = {},
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = fromCheckout) {
                onSelect()
                onSetDefault()
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected && fromCheckout) colors.primary.copy(alpha = 0.1f) else colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected && fromCheckout) 4.dp else 1.dp),
        border = if (selected && fromCheckout) BorderStroke(
            2.dp,
            colors.primary
        ) else BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (fromCheckout) {
                        RadioButton(
                            selected = selected,
                            onClick = {
                                onSelect()
                                onSetDefault()
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colors.primary
                            )
                        )
                    }
                    Text(
                        text = address.type.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.primaryText,
                        fontWeight = FontWeight.Bold
                    )
                    if (address.isDefault) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Default",
                            tint = Color.Green,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = address.fullName,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.primaryText,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = address.phone,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.secondaryText
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${address.addressLine1}${if (address.addressLine2 != null) ", ${address.addressLine2}" else ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.primaryText
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${address.city}, ${address.state} - ${address.postalCode}",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.primaryText
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onSetDefault,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (address.isDefault) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                        contentDescription = if (address.isDefault) "Default Address" else "Set as Default",
                        tint = if (address.isDefault) colors.primary else colors.secondaryText
                    )
                }
                Text(
                    text = if (address.isDefault) "Default Address" else "Set as Default",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (address.isDefault) colors.primary else colors.secondaryText
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAddressDialog(
    address: Address?,
    onDismiss: () -> Unit,
    onSave: (Address) -> Unit,
    viewModel: AddressViewModel,
    colors: com.example.localshop.core.designsystem.theme.AppColors,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(address?.fullName ?: "") }
    var phone by remember { mutableStateOf(address?.phone ?: "") }
    var addressLine1 by remember { mutableStateOf(address?.addressLine1 ?: "") }
    var addressLine2 by remember { mutableStateOf(address?.addressLine2 ?: "") }
    var city by remember { mutableStateOf(address?.city ?: "") }
    var state by remember { mutableStateOf(address?.state ?: "") }
    var postalCode by remember { mutableStateOf(address?.postalCode ?: "") }
    var addressType by remember { mutableStateOf(address?.type ?: "home") }
    var latitude by remember { mutableStateOf(address?.latitude) }
    var longitude by remember { mutableStateOf(address?.longitude) }
    var isGettingLocation by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isFormValid = name.isNotBlank() &&
            phone.isNotBlank() &&
            addressLine1.isNotBlank() &&
            city.isNotBlank() &&
            state.isNotBlank() &&
            postalCode.isNotBlank()

    LaunchedEffect(permissionDenied) {
        if (permissionDenied) {
            snackbarHostState.showSnackbar("Location permission is required to use current location")
            permissionDenied = false
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            errorMessage = null
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isGettingLocation = true
            viewModel.getCurrentLocation { locationResult ->
                when (locationResult) {
                    is com.example.localshop.core.location.LocationResult.Success -> {
                        latitude = locationResult.latitude
                        longitude = locationResult.longitude
                        viewModel.getAddressFromLocation(locationResult.latitude, locationResult.longitude) { addressResult ->
                            isGettingLocation = false
                            when (addressResult) {
                                is com.example.localshop.core.location.AddressResult.Success -> {
                                    addressLine1 = addressResult.addressLine1
                                    addressLine2 = addressResult.addressLine2
                                    city = addressResult.city
                                    state = addressResult.state
                                    postalCode = addressResult.postalCode
                                }
                                is com.example.localshop.core.location.AddressResult.NotFound -> {
                                    errorMessage = "Address not found for this location"
                                }
                                is com.example.localshop.core.location.AddressResult.Error -> {
                                    errorMessage = "Error fetching address: ${addressResult.message}"
                                }
                            }
                        }
                    }
                    is com.example.localshop.core.location.LocationResult.PermissionDenied -> {
                        isGettingLocation = false
                        permissionDenied = true
                    }
                    is com.example.localshop.core.location.LocationResult.LocationUnavailable -> {
                        isGettingLocation = false
                        permissionDenied = true
                    }
                }
            }
        } else {
            permissionDenied = true
        }
    }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (address != null) "Edit Address" else "Add Address",
                style = MaterialTheme.typography.titleLarge,
                color = colors.primaryText,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var isDropdownExpanded by remember { mutableStateOf(false) }
                val addressTypes = listOf("home", "work", "other")

                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = addressType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Address Type") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.clickable { isDropdownExpanded = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier.exposedDropdownSize()
                    ) {
                        addressTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }) },
                                onClick = {
                                    addressType = type
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = addressLine1,
                    onValueChange = { addressLine1 = it },
                    label = { Text("Address Line 1") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = addressLine2,
                    onValueChange = { addressLine2 = it },
                    label = { Text("Address Line 2 (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    label = { Text("Postal Code") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Current Location Button
                Button(
                    onClick = {
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                isGettingLocation = true
                                viewModel.getCurrentLocation { locationResult ->
                                    when (locationResult) {
                                        is com.example.localshop.core.location.LocationResult.Success -> {
                                            latitude = locationResult.latitude
                                            longitude = locationResult.longitude
                                            viewModel.getAddressFromLocation(locationResult.latitude, locationResult.longitude) { addressResult ->
                                                isGettingLocation = false
                                                when (addressResult) {
                                                    is com.example.localshop.core.location.AddressResult.Success -> {
                                                        addressLine1 = addressResult.addressLine1
                                                        addressLine2 = addressResult.addressLine2
                                                        city = addressResult.city
                                                        state = addressResult.state
                                                        postalCode = addressResult.postalCode
                                                    }
                                                    is com.example.localshop.core.location.AddressResult.NotFound -> {
                                                        errorMessage = "Address not found for this location"
                                                    }
                                                    is com.example.localshop.core.location.AddressResult.Error -> {
                                                        errorMessage = "Error fetching address: ${addressResult.message}"
                                                    }
                                                }
                                            }
                                        }
                                        is com.example.localshop.core.location.LocationResult.PermissionDenied -> {
                                            isGettingLocation = false
                                            permissionDenied = true
                                        }
                                        is com.example.localshop.core.location.LocationResult.LocationUnavailable -> {
                                            isGettingLocation = false
                                            permissionDenied = true
                                        }
                                    }
                                }
                            }
                            else -> {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isGettingLocation,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.secondaryButton,
                        contentColor = colors.primaryText
                    )
                ) {
                    if (isGettingLocation) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = colors.primaryText
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Use Current Location")
                    }
                }

                if (latitude != null && longitude != null) {
                    Text(
                        text = "Location captured: $latitude, $longitude",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Green
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newAddress = Address(
                        id = address?.id ?: 0,
                        userId = address?.userId ?: 0,
                        fullName = name,
                        phone = phone,
                        addressLine1 = addressLine1,
                        addressLine2 = addressLine2.ifBlank { null },
                        city = city,
                        state = state,
                        postalCode = postalCode,
                        type = addressType,
                        latitude = latitude,
                        longitude = longitude
                    )
                    onSave(newAddress)
                },
                enabled = isFormValid && !(viewModel.uiState.value.isAddingAddress || viewModel.uiState.value.isUpdatingAddress),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primaryButton,
                    contentColor = colors.primaryText
                )
            ) {
                if (viewModel.uiState.value.isAddingAddress || viewModel.uiState.value.isUpdatingAddress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colors.primaryText
                    )
                } else {
                    Text("Submit")
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.secondaryButton,
                    contentColor = colors.primaryText
                )
            ) {
                Text("Cancel")
            }
        }
    )
}
