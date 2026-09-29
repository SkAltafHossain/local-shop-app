# Authentication & Token Storage Flow

## Overview
The app uses **DataStore** (modern replacement for SharedPreferences) to securely store and retrieve the authentication bearer token.

## Architecture

### 1. DataStore Setup
**File:** `core/di/DataStoreModule.kt`
- Provides `DataStore<Preferences>` singleton
- Uses preference name: `localshop_prefs` (defined in Constants)
- Provides `TokenProvider` implementation

### 2. Token Storage
**File:** `core/network/TokenProviderImpl.kt`
- **saveToken(token: String)**: Stores the bearer token in DataStore
- **getToken()**: Retrieves token as a Flow<String?>
- **clearToken()**: Removes token from DataStore
- Storage key: `auth_token` (defined in Constants)

### 3. Login Flow
**File:** `feature/auth/data/repository/AuthRepositoryImpl.kt` (lines 139-156)

```kotlin
override fun login(email: String, password: String): Flow<ResultState<AuthResponse>> = flow {
    emit(ResultState.Loading)
    try {
        val request = LoginRequestDto(email, password)
        val response = authApi.login(request)
        if (response.success && response.data != null) {
            val authResponse = AuthMapper.mapToDomain(response.data)
            tokenProvider.saveToken(authResponse.token)  // ✅ Token saved here
            emit(ResultState.Success(authResponse))
        } else {
            val errorMessage = extractErrorMessageFromResponse(response)
            emit(ResultState.Error(errorMessage))
        }
    } catch (e: Exception) {
        val errorMessage = extractErrorMessage(e)
        emit(ResultState.Error(errorMessage))
    }
}
```

### 4. Register Flow
**File:** `feature/auth/data/repository/AuthRepositoryImpl.kt` (lines 120-137)

```kotlin
override fun register(name: String, email: String, phone: String, password: String, passwordConfirmation: String): Flow<ResultState<AuthResponse>> = flow {
    emit(ResultState.Loading)
    try {
        val request = RegisterRequestDto(name, email, password, passwordConfirmation, phone)
        val response = authApi.register(request)
        if (response.success && response.data != null) {
            val authResponse = AuthMapper.mapToDomain(response.data)
            tokenProvider.saveToken(authResponse.token)  // ✅ Token saved here
            emit(ResultState.Success(authResponse))
        } else {
            val errorMessage = extractErrorMessageFromResponse(response)
            emit(ResultState.Error(errorMessage))
        }
    } catch (e: Exception) {
        val errorMessage = extractErrorMessage(e)
        emit(ResultState.Error(errorMessage))
    }
}
```

### 5. Token Usage in API Requests
**File:** `core/network/AuthInterceptor.kt`

```kotlin
override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    
    // Skip auth for public endpoints
    val path = originalRequest.url.encodedPath
    if (isPublicEndpoint(path)) {
        return chain.proceed(originalRequest)
    }

    // Add auth token for protected endpoints
    val token = runBlocking { tokenProvider.getToken() }
    
    if (token != null) {
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")  // ✅ Bearer token added here
            .build()
        return chain.proceed(authenticatedRequest)
    }

    return chain.proceed(originalRequest)
}
```

### 6. Public vs Protected Endpoints

**Public Endpoints** (No token required):
- `/register`
- `/login`
- `/forgot-password`
- `/reset-password`
- `/shop/settings`
- `/shop/info`
- `/products`
- `/categories`

**Protected Endpoints** (Require Bearer token):
- `/cart` (all cart operations)
- `/orders` (checkout and order management)
- `/wishlist` (wishlist operations)
- `/user` (user profile and settings)
- `/user/addresses` (address management)

### 7. Logout Flow
**File:** `feature/auth/data/repository/AuthRepositoryImpl.kt` (lines 252-268)

```kotlin
override fun logout(): Flow<ResultState<Unit>> = flow {
    emit(ResultState.Loading)
    try {
        val response = authApi.logout()
        tokenProvider.clearToken()  // ✅ Token cleared here
        if (response.success) {
            emit(ResultState.Success(Unit))
        } else {
            val errorMessage = extractErrorMessageFromResponse(response)
            emit(ResultState.Error(errorMessage))
        }
    } catch (e: Exception) {
        tokenProvider.clearToken()  // ✅ Token cleared even on error
        val errorMessage = extractErrorMessage(e)
        emit(ResultState.Error(errorMessage))
    }
}
```

## Data Model

### API Response Structure
**File:** `feature/auth/data/remote/dto/UserDto.kt`

```kotlin
@Serializable
data class AuthResponseDto(
    @SerialName("token")
    val token: String,
    
    @SerialName("token_type")
    val tokenType: String? = null,
    
    @SerialName("user")
    val user: UserDto
)
```

### Domain Model
**File:** `feature/auth/domain/model/User.kt`

```kotlin
data class AuthResponse(
    val token: String,
    val tokenType: String? = null,
    val user: User
)
```

## Constants
**File:** `core/common/Constants.kt`

```kotlin
object Constants {
    const val BASE_URL = "http://192.168.31.228:8001/api/"
    const val PREF_NAME = "localshop_prefs"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_NAME = "user_name"
    const val KEY_USER_EMAIL = "user_email"
    
    const val DEFAULT_PAGE_SIZE = 12
    const val CONNECTION_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}
```

## Summary

✅ **Token Storage**: DataStore (modern SharedPreferences replacement)
✅ **Login**: Token saved after successful login
✅ **Register**: Token saved after successful registration
✅ **Authentication**: Bearer token automatically added to protected endpoints
✅ **Logout**: Token cleared on logout
✅ **Session Management**: SessionManager tracks current user and login state

The authentication flow is complete and follows Android best practices using DataStore for secure token storage.
