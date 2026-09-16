# AI Coding Prompt: Local Shop Android Application

## 1. Project Overview

Build a complete, production-ready Android e-commerce application for a local shop using:

* Kotlin
* Jetpack Compose with Material 3
* MVVM (Model–View–ViewModel)
* Clean Architecture
* Use Case / Interactor pattern
* Kotlin Coroutines and Flow
* Hilt for Dependency Injection
* Retrofit and OkHttp for REST API communication
* Kotlinx Serialization or another consistent JSON serialization solution
* Navigation Compose
* Room for local persistence and caching where appropriate
* DataStore for preferences and non-sensitive session settings
* Coil for product image loading
* Gradle Kotlin DSL with version catalog (`libs.versions.toml`)

The application must use the supplied Laravel REST API as its backend.

**Important:** Build a real, API-connected application, not a static UI prototype. All API-dependent screens must use repository interfaces, repository implementations, use cases, ViewModels, and observable UI state.

Do not invent backend fields, request parameters, response properties, or endpoint behavior. When the API documentation does not specify a schema, isolate the uncertainty in a clearly documented model or adapter and request the actual schema before implementing unsupported assumptions.

---

## 2. Initial Android Project Setup

Create a new Android Studio project with the following configuration:

* Project name: `LocalShop`
* Application ID: `com.example.localshop` (make configurable)
* Language: Kotlin
* UI: Jetpack Compose
* Minimum SDK: choose a suitable minimum supported Android version
* Compile SDK and target SDK: use the latest stable versions supported by the selected Android Gradle Plugin
* Gradle: Kotlin DSL
* Build configuration: version catalog
* Dependency Injection: Hilt
* Architecture: feature-oriented Clean Architecture

Use stable, mutually compatible library versions. Do not use alpha, beta, or deprecated libraries unless explicitly required.

Create all necessary project files, Gradle configuration, manifest, themes, application class, navigation setup, and package structure.

Ensure the project can sync, compile, and run in Android Studio.

---

## 3. Required Project Structure

Use a scalable, feature-first architecture with separate domain, data, and presentation layers.

```text
LocalShop/
│
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
│
├── gradle/
│   └── libs.versions.toml
│
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/localshop/
│       │   │
│       │   ├── LocalShopApp.kt
│       │   ├── MainActivity.kt
│       │   │
│       │   ├── core/
│       │   │   ├── common/
│       │   │   ├── designsystem/
│       │   │   ├── navigation/
│       │   │   ├── network/
│       │   │   ├── database/
│       │   │   ├── datastore/
│       │   │   ├── di/
│       │   │   ├── error/
│       │   │   ├── result/
│       │   │   └── util/
│       │   │
│       │   ├── feature/
│       │   │   ├── auth/
│       │   │   ├── home/
│       │   │   ├── product/
│       │   │   ├── category/
│       │   │   ├── search/
│       │   │   ├── cart/
│       │   │   ├── wishlist/
│       │   │   ├── address/
│       │   │   ├── checkout/
│       │   │   ├── orders/
│       │   │   ├── profile/
│       │   │   └── settings/
│       │   │
│       │   └── res/
│       │       ├── values/
│       │       ├── drawable/
│       │       └── mipmap/
│       │
│       └── test/
│
└── README.md
```

Each feature must follow this internal structure:

```text
feature/
└── products/
    ├── data/
    │   ├── remote/
    │   │   ├── ProductApi.kt
    │   │   └── dto/
    │   ├── mapper/
    │   │   └── ProductMapper.kt
    │   └── repository/
    │       └── ProductRepositoryImpl.kt
    │
    ├── domain/
    │   ├── model/
    │   │   └── Product.kt
    │   ├── repository/
    │   │   └── ProductRepository.kt
    │   └── usecase/
    │       ├── GetProductsUseCase.kt
    │       ├── GetProductDetailsUseCase.kt
    │       ├── SearchProductsUseCase.kt
    │       └── GetFeaturedProductsUseCase.kt
    │
    └── presentation/
        ├── screen/
        │   ├── ProductListScreen.kt
        │   └── ProductDetailsScreen.kt
        ├── component/
        ├── viewmodel/
        │   └── ProductViewModel.kt
        └── state/
            └── ProductUiState.kt
```

Apply this pattern consistently to all features.

Do not put every API service, repository, ViewModel, or screen inside a single large file.

Keep domain models independent of Retrofit, Compose, Android Context, and database entities.

---

## 4. Clean Architecture Rules

### Presentation layer

Responsible for:

* Jetpack Compose screens and reusable UI components
* ViewModels
* UI state and UI events
* User input validation where appropriate
* Navigation events
* Collecting Flow using lifecycle-aware APIs

The presentation layer must not call Retrofit services or data sources directly.

### Domain layer

Responsible for:

* Business models
* Repository interfaces
* Use cases
* Business rules and validation

Use cases must depend on repository interfaces, not repository implementations.

Keep use cases focused on one business operation.

Example:

```kotlin
class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(
        filters: ProductFilters
    ): Flow<ResultState<PagedResult<Product>>> {
        return repository.getProducts(filters)
    }
}
```

Adapt the example to the actual shared result types and API response schema.

Do not create unnecessary use cases that simply duplicate unrelated functionality. However, create dedicated use cases for meaningful operations such as adding to cart, placing an order, logging in, or updating an address.

### Data layer

Responsible for:

* Retrofit API interfaces
* Request and response DTOs
* Repository implementations
* DTO-to-domain model mapping
* Room entities and DAO implementations
* Data persistence
* API error parsing

Repositories must hide the data source details from the domain layer.

---

## 5. Dependency Injection

Configure Hilt throughout the application.

Create appropriate modules:

```text
core/di/
├── NetworkModule.kt
├── RepositoryModule.kt
├── DatabaseModule.kt
├── DataStoreModule.kt
└── UseCaseModule.kt (only if necessary)
```

Requirements:

1. Add the required Hilt Gradle plugins and dependencies.
2. Create an Application class annotated with `@HiltAndroidApp`.
3. Annotate MainActivity with `@AndroidEntryPoint`.
4. Use `@HiltViewModel` for ViewModels.
5. Bind repository interfaces to their implementations using Hilt.
6. Provide Retrofit, OkHttp, API interfaces, Room, and DataStore through appropriate modules.
7. Avoid manual dependency construction inside ViewModels.
8. Use constructor injection wherever possible.

---

## 6. Network Configuration

Create a centralized Retrofit and OkHttp configuration.

Base URL:

```text
https://your-domain.com/api/
```

The supplied documentation uses:

```text
http://your-domain.com/api
```

Replace this placeholder with the actual backend URL.

Requirements:

* Use a configurable base URL.
* Configure JSON serialization.
* Set reasonable connection, read, and write timeouts.
* Add an Authorization interceptor for protected endpoints.
* Add a centralized error-handling mechanism.
* Support HTTP 200 and 201 success responses.
* Handle HTTP 400, 401, 403, 404, 422, and 500.
* Handle network disconnection, timeout, malformed JSON, and unexpected errors.
* Never expose access tokens or passwords in logs.
* Do not hardcode production credentials.
* Do not disable TLS verification or use insecure SSL workarounds.

For local development, support an explicitly configured development API URL. Android's emulator uses `10.0.2.2` to access the host machine's localhost. A physical device requires a reachable LAN address or deployed backend URL.

Use HTTPS in production.

---

## 7. API Response Handling

The backend documents this success response:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}
```

Error response:

```json
{
  "success": false,
  "message": "Error message",
  "errors": {}
}
```

Create reusable response DTOs and error handling.

Example conceptual structure:

```kotlin
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
```

Adapt serialization and nullable fields to the actual server responses.

Important:

* Do not assume every endpoint returns the same shape.
* Do not assume `data` is always an object; some endpoints may return arrays.
* Do not assume the error `errors` object has a fixed structure.
* Support Laravel validation errors, including field-specific messages.
* Handle endpoints that return an empty response body.
* Preserve useful server error messages for display.
* Use typed DTOs for documented response fields.
* Use explicit endpoint-specific response models when response formats differ.

For paginated endpoints, support:

```json
{
  "success": true,
  "data": [],
  "pagination": {
    "total": 50,
    "per_page": 12,
    "current_page": 1,
    "last_page": 5,
    "from": 1,
    "to": 12
  }
}
```

Create a pagination model and support loading additional pages.

Do not assume that all endpoints use this pagination format without checking their actual responses.

---

## 8. Authentication and Token Management

Implement:

* Registration
* Login
* Forgot password
* Reset password
* Logout
* Current user profile
* Update profile
* Change password

Authentication endpoints:

```text
POST /register
POST /login
POST /forgot-password
POST /reset-password
GET  /user
PUT  /user
PUT  /user/password
POST /logout
```

All paths are relative to the configured `/api/` base URL.

Create:

```text
feature/auth/
├── data/
├── domain/
└── presentation/
```

Required use cases:

```text
RegisterUserUseCase
LoginUseCase
ForgotPasswordUseCase
ResetPasswordUseCase
LogoutUseCase
GetCurrentUserUseCase
UpdateUserProfileUseCase
UpdatePasswordUseCase
```

Requirements:

1. Store the authentication token securely using an appropriate Android secure-storage solution.
2. Do not store passwords.
3. Attach the token to protected API requests:

```http
Authorization: Bearer {token}
```

4. Do not attach the token to public endpoints unnecessarily.
5. On successful login, persist the token and update the application authentication state.
6. On logout, call the logout endpoint and clear local session data according to the application's session policy.
7. Handle expired or invalid tokens and HTTP 401 responses.
8. Do not assume the login response contains a token under a particular JSON key until the backend response is confirmed.
9. Support password reset using the actual request fields required by Laravel.
10. Do not log sensitive authentication data.

Use a centralized authentication/session manager and expose authentication state as Flow.

---

## 9. Shop Settings and Home Screen

Public endpoints:

```text
GET /shop/settings
GET /shop/info
```

Create:

```text
feature/home/
feature/settings/
```

Required use cases:

```text
GetShopSettingsUseCase
GetShopInfoUseCase
```

The Home screen should include:

* Shop information
* Search bar
* Featured products
* Latest products
* Product categories
* Product listing entry point
* Cart entry point
* Wishlist entry point
* Loading, error, empty, and success states

Use the actual shop settings response to configure supported information such as shop name, logo, or contact details.

Do not assume the backend provides banners, promotions, or delivery charges unless the response confirms those fields.

---

## 10. Product Feature

Public endpoints:

```text
GET /products
GET /products/{id}
GET /products/search
GET /products/featured
GET /products/latest
```

Create:

```text
feature/product/
feature/search/
```

Required use cases:

```text
GetProductsUseCase
GetProductDetailsUseCase
SearchProductsUseCase
GetFeaturedProductsUseCase
GetLatestProductsUseCase
```

Product listing must support these documented filters:

```text
category
min_price
max_price
search
sort
per_page
```

Supported sorting values:

```text
price_low
price_high
name_asc
name_desc
newest
```

Default sorting is ID ascending.

Price filtering and price sorting use the discount price when available.

Implement:

* Product listing screen
* Product details screen
* Related products
* Product search
* Featured products
* Latest products
* Category filter
* Price filter
* Sorting selector
* Pagination
* Pull-to-refresh
* Add-to-cart action
* Add-to-wishlist action

Use stable product IDs for list item keys.

Do not invent the product DTO. First inspect the actual product JSON response and implement the corresponding DTO, domain model, and mapper.

---

## 11. Category Feature

Public endpoints:

```text
GET /categories
GET /categories/{id}
GET /categories/{id}/products
```

Required use cases:

```text
GetCategoriesUseCase
GetCategoryDetailsUseCase
GetCategoryProductsUseCase
```

Implement:

* Category list
* Category details
* Products belonging to a category
* Navigation from category to product list
* Pagination if the API provides it
* Loading, error, and empty states

Use category slugs for product filters where required by the API.

---

## 12. Shopping Cart Feature

Protected endpoints:

```text
GET    /cart
POST   /cart
PUT    /cart/{id}
DELETE /cart/{id}
DELETE /cart
```

Required use cases:

```text
GetCartUseCase
AddToCartUseCase
UpdateCartItemQuantityUseCase
RemoveCartItemUseCase
ClearCartUseCase
```

Implement:

* Cart listing
* Quantity increment and decrement
* Remove item
* Clear cart
* Cart loading and error states
* Cart item count
* Cart subtotal where sufficient price data is available
* Checkout navigation

Add-to-cart request:

```json
{
  "product_id": 1,
  "quantity": 2
}
```

Requirements:

* Prevent invalid quantities.
* Disable duplicate submissions while a request is in progress.
* Handle stock or validation errors returned by the server.
* Refresh or reconcile the cart after mutations.
* Do not assume cart totals include taxes, delivery charges, or discounts unless documented.
* The backend remains the source of truth for cart prices and item availability.

---

## 13. Wishlist Feature

Protected endpoints:

```text
GET    /wishlist
POST   /wishlist
DELETE /wishlist/{id}
POST   /wishlist/check
```

Required use cases:

```text
GetWishlistUseCase
AddToWishlistUseCase
RemoveFromWishlistUseCase
CheckWishlistUseCase
```

Implement:

* Wishlist screen
* Add and remove products
* Wishlist button on product cards
* Wishlist button on product details
* Check whether a product is in the wishlist
* Loading, empty, and error states

Do not assume the request body or response structure of the wishlist check endpoint. Verify the actual backend contract first.

---

## 14. Address Feature

Protected endpoints:

```text
GET    /user/addresses
POST   /user/addresses
PUT    /user/addresses/{id}
DELETE /user/addresses/{id}
PUT    /user/addresses/{id}/default
```

Required use cases:

```text
GetAddressesUseCase
AddAddressUseCase
UpdateAddressUseCase
DeleteAddressUseCase
SetDefaultAddressUseCase
```

Implement:

* Address listing
* Add address form
* Edit address form
* Delete address confirmation
* Set default address
* Select delivery address during checkout

Use actual Laravel validation rules and address response fields.

Do not assume the address schema includes fields such as state, city, postal code, landmark, or address type until confirmed.

---

## 15. Checkout and Order Feature

Protected endpoints:

```text
GET  /orders
GET  /orders/{id}
POST /orders
PUT  /orders/{id}/cancel
PUT  /orders/{id}/confirm-delivery
```

Required use cases:

```text
GetOrdersUseCase
GetOrderDetailsUseCase
CreateOrderUseCase
CancelOrderUseCase
ConfirmOrderDeliveryUseCase
```

Create the following screens:

* Checkout
* Address selection
* Order summary
* Payment method selection
* Order confirmation
* Order history
* Order details

Create order request:

```json
{
  "address_id": 1,
  "payment_method": "cod",
  "items": [
    {
      "product_id": 1,
      "quantity": 2
    }
  ]
}
```

Implement the documented Cash on Delivery payment method.

Important:

* Do not invent online payment APIs.
* Do not assume an order is successfully placed until the server confirms it.
* Prevent duplicate order submissions.
* Handle server-side stock, price, and validation errors.
* Display the actual order ID and status returned by the backend.
* Allow cancellation and delivery confirmation only when supported by the server.
* Do not invent order status values or cancellation rules.

---

## 16. MVVM and UI State Management

Every feature must have a dedicated ViewModel and immutable UI state.

Example:

```kotlin
data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val isLoadingMore: Boolean = false
)
```

Example ViewModel:

```kotlin
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ProductUiState())

    val uiState: StateFlow<ProductUiState> =
        _uiState.asStateFlow()

    fun loadProducts(filters: ProductFilters) {
        viewModelScope.launch {
            // Execute use case.
            // Update loading, success, and error states.
        }
    }
}
```

Complete the implementation with the shared result type, error handling, cancellation, and pagination logic.

Requirements:

* Use `StateFlow` for observable UI state.
* Use `viewModelScope` for ViewModel operations.
* Collect state using `collectAsStateWithLifecycle()`.
* Keep business logic out of Composable functions.
* Avoid putting API calls in `LaunchedEffect` without a clear lifecycle reason.
* Prevent repeated network requests caused by recomposition.
* Handle concurrent requests safely.
* Use immutable state updates.
* Expose one-time navigation or notification events through an appropriate event mechanism.
* Never expose mutable state directly to the UI.

---

## 17. Shared Core Components

Create reusable infrastructure.

### Network

```text
core/network/
├── ApiResponse.kt
├── NetworkModule.kt
├── AuthInterceptor.kt
├── TokenProvider.kt
├── NetworkConnectivity.kt
└── NetworkErrorParser.kt
```

### Error handling

```text
core/error/
├── AppError.kt
├── ErrorMapper.kt
└── ValidationError.kt
```

### Result handling

```text
core/result/
├── ResultState.kt
└── PagedResult.kt
```

Use a consistent result representation for loading, success, and failure.

### Design system

```text
core/designsystem/
├── theme/
├── component/
│   ├── AppButton.kt
│   ├── AppTextField.kt
│   ├── LoadingIndicator.kt
│   ├── ErrorView.kt
│   ├── EmptyState.kt
│   ├── ProductCard.kt
│   └── PriceText.kt
└── typography/
```

Create reusable components without coupling them to specific ViewModels or repositories.

---

## 18. Navigation Structure

Use Navigation Compose with type-safe routes if supported by the selected stable library version.

Required destinations:

```text
Splash / Session Check
    ├── Login
    ├── Register
    ├── Forgot Password
    └── Main Application
          ├── Home
          ├── Categories
          ├── Search
          ├── Product Details
          ├── Cart
          ├── Wishlist
          ├── Checkout
          ├── Address Management
          ├── Order History
          ├── Order Details
          ├── Profile
          └── Settings
```

Requirements:

* Use a centralized navigation graph.
* Do not pass complete product or order objects between destinations.
* Pass stable IDs and load the required data through use cases.
* Protect authenticated destinations.
* Preserve appropriate back-stack behavior.
* Do not create duplicate destinations when handling repeated navigation events.

---

## 19. UI and User Experience

Use Material 3 and a consistent e-commerce design system.

Design the application with:

* Responsive layouts
* Product grids
* Product image loading
* Product price and discount display
* Clear loading indicators
* Empty states
* Error states with retry
* Form validation
* Confirmation dialogs for destructive actions
* Accessible content descriptions
* Dark and light themes
* Proper keyboard handling
* Lifecycle-aware state collection

The application should be suitable for a local shop and work on both small and large Android screens.

Do not create fake products or hardcoded prices in production screens.

If mock data is needed for previews or tests, keep it in a separate mock or preview source.

---

## 20. Pagination and Data Caching

Implement pagination for product lists and other endpoints that provide pagination metadata.

Requirements:

* Track current page and last page.
* Prevent multiple requests for the same page.
* Append new results without duplicating items.
* Reset pagination when filters change.
* Support loading and retry states.
* Respect the backend's actual pagination parameters and metadata.

Use Room selectively for useful local caching, such as recently viewed products or cached catalog data.

Do not cache sensitive authentication data in Room.

Do not assume that the backend supports offline writes or synchronization. Clearly distinguish cached information from current server data.

---

## 21. Security and Reliability

Implement:

* Secure authentication token storage
* HTTPS in production
* No hardcoded secrets
* No token or password logging
* Proper HTTP error handling
* Form validation
* Duplicate-request prevention
* Safe session expiration handling
* Appropriate Android backup configuration for sensitive data
* No insecure SSL certificate bypasses

Avoid automatically retrying non-idempotent requests such as order creation unless the backend supports a reliable idempotency mechanism.

---

## 22. Testing Requirements

Add unit tests for:

* Use cases
* Repository implementations
* DTO-to-domain mapping
* ViewModels
* Validation logic
* Pagination
* Error handling

Add UI tests for:

* Login and registration
* Product browsing
* Product details
* Cart interactions
* Checkout navigation
* Order history

Use fake repositories for ViewModel tests.

Use MockWebServer or an equivalent testing solution for network tests.

Ensure that tests do not require production credentials or a live production server.

---

## 23. Implementation Order

Implement the project in the following order:

### Phase 1: Project foundation

* Create Android project.
* Configure Gradle and version catalog.
* Add dependencies.
* Configure Hilt.
* Configure Material 3.
* Create navigation and application entry point.
* Confirm the application compiles.

### Phase 2: Core architecture

* Create shared result and error models.
* Configure Retrofit and OkHttp.
* Configure authentication token handling.
* Create shared network and dependency injection modules.
* Create common UI components.

### Phase 3: Public catalog

* Shop settings and shop information.
* Product list.
* Product details.
* Featured products.
* Latest products.
* Categories.
* Search and filters.
* Pagination.

### Phase 4: Authentication

* Registration.
* Login.
* Forgot password.
* Reset password.
* Session persistence.
* Logout.
* User profile.

### Phase 5: Shopping features

* Cart.
* Wishlist.
* Address management.

### Phase 6: Orders

* Checkout.
* Create order.
* Order history.
* Order details.
* Cancel order.
* Confirm delivery.

### Phase 7: Finalization

* Add tests.
* Fix compilation issues.
* Fix navigation issues.
* Verify API integration.
* Improve accessibility.
* Update README with setup instructions.

---

## 24. API Documentation Integration

Use the following Laravel API contract as the source of truth.

### Base URL

```text
http://your-domain.com/api
```

Configure Retrofit with the trailing slash:

```text
http://your-domain.com/api/
```

All paths below are relative to this base URL. Do not add `/api` a second time in Retrofit endpoint annotations.

### Public authentication endpoints

```text
POST /register
POST /login
POST /forgot-password
POST /reset-password
```

### Public shop endpoints

```text
GET /shop/settings
GET /shop/info
```

### Public product endpoints

```text
GET /products
GET /products/{id}
GET /products/search
GET /products/featured
GET /products/latest
```

### Public category endpoints

```text
GET /categories
GET /categories/{id}
GET /categories/{id}/products
```

### Protected user endpoints

```text
GET  /user
PUT  /user
PUT  /user/password
POST /logout
```

### Protected address endpoints

```text
GET    /user/addresses
POST   /user/addresses
PUT    /user/addresses/{id}
DELETE /user/addresses/{id}
PUT    /user/addresses/{id}/default
```

### Protected cart endpoints

```text
GET    /cart
POST   /cart
PUT    /cart/{id}
DELETE /cart/{id}
DELETE /cart
```

### Protected order endpoints

```text
GET /orders
GET /orders/{id}
POST /orders
PUT /orders/{id}/cancel
PUT /orders/{id}/confirm-delivery
```

### Protected wishlist endpoints

```text
GET    /wishlist
POST   /wishlist
DELETE /wishlist/{id}
POST   /wishlist/check
```

### Product query parameters

```text
category
min_price
max_price
search
sort
per_page
```

Sorting values:

```text
price_low
price_high
name_asc
name_desc
newest
```

Default sorting: ID ascending.

Default `per_page`: 12.

Discount price must be used for price filtering and sorting when available.

### Authentication

Protected requests require:

```http
Authorization: Bearer {token}
```

### Error status codes

```text
200 Success
201 Created
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
422 Validation Error
500 Server Error
```

Use the documented success, error, and pagination formats described above.

---

## 25. Final Deliverables

Generate the complete Android Studio project with:

1. All Gradle configuration files.
2. A working application entry point.
3. All required feature packages.
4. Domain models and repository interfaces.
5. Data DTOs, API services, mappers, and repository implementations.
6. Use cases for the documented business operations.
7. ViewModels and immutable UI state.
8. Jetpack Compose screens and reusable components.
9. Hilt dependency injection.
10. Retrofit and OkHttp configuration.
11. Authentication and token management.
12. Navigation graph.
13. Appropriate Room and DataStore implementations.
14. Unit and UI tests.
15. README with complete setup instructions.

### Strict coding rules

* Generate real Kotlin source files, not pseudocode.
* Do not skip files by writing “implement similarly.”
* Do not create a monolithic ViewModel or repository.
* Do not place business logic in Composable functions.
* Do not call API services directly from screens.
* Do not use fake API responses in production code.
* Do not invent undocumented request or response fields.
* Do not create payment integrations that are absent from the API.
* Do not claim that the project compiles unless compilation has actually been verified.
* Do not leave empty functions, unresolved references, or unnecessary TODOs in the final implementation.

If the complete project is too large to generate in one response, implement it phase by phase. Keep the architecture, naming, models, and interfaces consistent across all phases.

Start by generating the project foundation, Gradle configuration, application class, network layer, shared result and error handling, and Hilt setup. Then proceed feature by feature.

Before implementing API-dependent models, ask for the actual JSON responses or Laravel controller/resource definitions wherever the supplied documentation is insufficient.

The final application must be maintainable, testable, scalable, and ready for integration with the actual Local Shop Laravel backend.
