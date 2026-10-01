package com.example.localshop.feature.address.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.location.LocationManager
import com.example.localshop.core.location.LocationResult
import com.example.localshop.core.location.AddressResult
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.address.domain.model.Address
import com.example.localshop.feature.address.domain.usecase.AddAddressUseCase
import com.example.localshop.feature.address.domain.usecase.DeleteAddressUseCase
import com.example.localshop.feature.address.domain.usecase.GetAddressesUseCase
import com.example.localshop.feature.address.domain.usecase.SetDefaultAddressUseCase
import com.example.localshop.feature.address.domain.usecase.UpdateAddressUseCase
import com.example.localshop.feature.address.presentation.state.AddressUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val addAddressUseCase: AddAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val setDefaultAddressUseCase: SetDefaultAddressUseCase,
    private val locationManager: LocationManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    fun getCurrentLocation(callback: (LocationResult) -> Unit) {
        viewModelScope.launch {
            val result = locationManager.getCurrentLocation()
            callback(result)
        }
    }

    fun getAddressFromLocation(latitude: Double, longitude: Double, callback: (AddressResult) -> Unit) {
        viewModelScope.launch {
            val result = locationManager.getAddressFromLocation(latitude, longitude)
            callback(result)
        }
    }

    fun loadAddresses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            getAddressesUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            addresses = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAddingAddress = true,
                errorMessage = null,
                addAddressSuccess = false
            )

            addAddressUseCase(address).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        // Reload addresses to get the updated list
                        loadAddresses()
                        _uiState.value = _uiState.value.copy(
                            isAddingAddress = false,
                            addAddressSuccess = true
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isAddingAddress = false
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun updateAddress(addressId: Int, address: Address) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isUpdatingAddress = true,
                errorMessage = null,
                updateAddressSuccess = false
            )

            updateAddressUseCase(addressId, address).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        // Reload addresses to get the updated list
                        loadAddresses()
                        _uiState.value = _uiState.value.copy(
                            isUpdatingAddress = false,
                            updateAddressSuccess = true
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isUpdatingAddress = false
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun deleteAddress(addressId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeletingAddress = true,
                errorMessage = null,
                deleteAddressSuccess = false
            )

            deleteAddressUseCase(addressId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        // Reload addresses to get the updated list
                        loadAddresses()
                        _uiState.value = _uiState.value.copy(
                            isDeletingAddress = false,
                            deleteAddressSuccess = true
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isDeletingAddress = false
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun setDefaultAddress(addressId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSettingDefault = true,
                errorMessage = null,
                setDefaultSuccess = false
            )

            setDefaultAddressUseCase(addressId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        // Reload addresses to get the updated list
                        loadAddresses()
                        _uiState.value = _uiState.value.copy(
                            isSettingDefault = false,
                            setDefaultSuccess = true
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isSettingDefault = false
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSuccessFlags() {
        _uiState.value = _uiState.value.copy(
            addAddressSuccess = false,
            updateAddressSuccess = false,
            deleteAddressSuccess = false,
            setDefaultSuccess = false
        )
    }
}
