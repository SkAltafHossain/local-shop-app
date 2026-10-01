package com.example.localshop.core.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationManager @Inject constructor(
    private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    suspend fun getCurrentLocation(): LocationResult {
        return suspendCancellableCoroutine { continuation ->
            if (!hasLocationPermission()) {
                continuation.resume(LocationResult.PermissionDenied)
                return@suspendCancellableCoroutine
            }

            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(
                            LocationResult.Success(
                                latitude = location.latitude,
                                longitude = location.longitude
                            )
                        )
                    } else {
                        continuation.resume(LocationResult.LocationUnavailable)
                    }
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): AddressResult {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                AddressResult.Success(
                    addressLine1 = address.getAddressLine(0) ?: "",
                    addressLine2 = if (address.subThoroughfare != null && address.thoroughfare != null) {
                        "${address.subThoroughfare} ${address.thoroughfare}"
                    } else address.thoroughfare ?: "",
                    city = address.locality ?: address.subAdminArea ?: "",
                    state = address.adminArea ?: "",
                    postalCode = address.postalCode ?: ""
                )
            } else {
                AddressResult.NotFound
            }
        } catch (e: IOException) {
            AddressResult.Error("Network error: ${e.message}")
        } catch (e: Exception) {
            AddressResult.Error("Error: ${e.message}")
        }
    }
}

sealed class LocationResult {
    data class Success(val latitude: Double, val longitude: Double) : LocationResult()
    object PermissionDenied : LocationResult()
    object LocationUnavailable : LocationResult()
}

sealed class AddressResult {
    data class Success(
        val addressLine1: String,
        val addressLine2: String,
        val city: String,
        val state: String,
        val postalCode: String
    ) : AddressResult()
    object NotFound : AddressResult()
    data class Error(val message: String) : AddressResult()
}
