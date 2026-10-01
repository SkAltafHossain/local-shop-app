package com.example.localshop.feature.address.presentation.state

import com.example.localshop.feature.address.domain.model.Address

data class AddressUiState(
    val isLoading: Boolean = false,
    val addresses: List<Address> = emptyList(),
    val errorMessage: String? = null,
    val isAddingAddress: Boolean = false,
    val isUpdatingAddress: Boolean = false,
    val isDeletingAddress: Boolean = false,
    val isSettingDefault: Boolean = false,
    val addAddressSuccess: Boolean = false,
    val updateAddressSuccess: Boolean = false,
    val deleteAddressSuccess: Boolean = false,
    val setDefaultSuccess: Boolean = false
)
