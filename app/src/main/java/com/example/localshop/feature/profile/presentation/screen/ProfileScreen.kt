package com.example.localshop.feature.profile.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Help

import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.localshop.core.designsystem.component.AppButton
import com.example.localshop.core.designsystem.component.AppOutlinedButton
import com.example.localshop.core.designsystem.component.ErrorModal
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.navigation.Screen
import com.example.localshop.feature.profile.presentation.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoggingOut by viewModel.isLoggingOut.collectAsState()
    val logoutError by viewModel.logoutError.collectAsState()
    val colors = AppTheme.colors

    // Modal states
    val showNameModal by viewModel.showNameModal.collectAsState()
    val showEmailModal by viewModel.showEmailModal.collectAsState()
    val showPhoneModal by viewModel.showPhoneModal.collectAsState()
    val showPasswordModal by viewModel.showPasswordModal.collectAsState()
    val showDeleteAccountModal by viewModel.showDeleteAccountModal.collectAsState()

    // Loading states
    val isUpdatingName by viewModel.isUpdatingName.collectAsState()
    val isUpdatingEmail by viewModel.isUpdatingEmail.collectAsState()
    val isUpdatingPhone by viewModel.isUpdatingPhone.collectAsState()
    val isUpdatingPassword by viewModel.isUpdatingPassword.collectAsState()
    val isDeletingAccount by viewModel.isDeletingAccount.collectAsState()

    // Error/Success states
    val updateError by viewModel.updateError.collectAsState()
    val updateSuccess by viewModel.updateSuccess.collectAsState()

    if (!isLoggedIn) {
        NotLoggedInProfileScreen(navController, colors)
    } else {
        LoggedInProfileScreen(
            navController = navController,
            colors = colors,
            onLogout = { viewModel.logout() },
            isLoggingOut = isLoggingOut,
            user = currentUser,
            viewModel = viewModel
        )
    }

    // Logout Error Modal
    if (logoutError != null) {
        ErrorModal(
            errorMessage = logoutError!!,
            onDismiss = { viewModel.clearLogoutError() }
        )
    }

    // Update Name Modal
    if (showNameModal) {
        val nameState = remember { mutableStateOf(currentUser?.name ?: "") }
        com.example.localshop.core.designsystem.component.InputModal(
            title = "Update Name",
            fields = listOf(
                com.example.localshop.core.designsystem.component.ModalField(
                    value = nameState.value,
                    label = "Name",
                    onValueChange = { nameState.value = it }
                )
            ),
            onDismiss = { viewModel.hideNameModal() },
            onConfirm = { viewModel.updateName(nameState.value) },
            isLoading = isUpdatingName,
            error = updateError,
            onErrorClear = { viewModel.clearUpdateError() }
        )
    }

    // Update Email Modal
    if (showEmailModal) {
        val emailState = remember { mutableStateOf(currentUser?.email ?: "") }
        val passwordState = remember { mutableStateOf("") }
        com.example.localshop.core.designsystem.component.InputModal(
            title = "Update Email",
            fields = listOf(
                com.example.localshop.core.designsystem.component.ModalField(
                    value = emailState.value,
                    label = "New Email",
                    onValueChange = { emailState.value = it },
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
                ),
                com.example.localshop.core.designsystem.component.ModalField(
                    value = passwordState.value,
                    label = "Current Password",
                    onValueChange = { passwordState.value = it },
                    isPassword = true
                )
            ),
            onDismiss = { viewModel.hideEmailModal() },
            onConfirm = { viewModel.updateEmail(emailState.value, passwordState.value) },
            isLoading = isUpdatingEmail,
            error = updateError,
            onErrorClear = { viewModel.clearUpdateError() }
        )
    }

    // Update Phone Modal
    if (showPhoneModal) {
        val phoneState = remember { mutableStateOf(currentUser?.phone ?: "") }
        val passwordState = remember { mutableStateOf("") }
        com.example.localshop.core.designsystem.component.InputModal(
            title = "Update Phone",
            fields = listOf(
                com.example.localshop.core.designsystem.component.ModalField(
                    value = phoneState.value,
                    label = "New Phone",
                    onValueChange = { phoneState.value = it },
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
                ),
                com.example.localshop.core.designsystem.component.ModalField(
                    value = passwordState.value,
                    label = "Current Password",
                    onValueChange = { passwordState.value = it },
                    isPassword = true
                )
            ),
            onDismiss = { viewModel.hidePhoneModal() },
            onConfirm = { viewModel.updatePhone(phoneState.value, passwordState.value) },
            isLoading = isUpdatingPhone,
            error = updateError,
            onErrorClear = { viewModel.clearUpdateError() }
        )
    }

    // Update Password Modal
    if (showPasswordModal) {
        val currentPasswordState = remember { mutableStateOf("") }
        val newPasswordState = remember { mutableStateOf("") }
        val confirmPasswordState = remember { mutableStateOf("") }
        com.example.localshop.core.designsystem.component.InputModal(
            title = "Update Password",
            fields = listOf(
                com.example.localshop.core.designsystem.component.ModalField(
                    value = currentPasswordState.value,
                    label = "Current Password",
                    onValueChange = { currentPasswordState.value = it },
                    isPassword = true
                ),
                com.example.localshop.core.designsystem.component.ModalField(
                    value = newPasswordState.value,
                    label = "New Password",
                    onValueChange = { newPasswordState.value = it },
                    isPassword = true
                ),
                com.example.localshop.core.designsystem.component.ModalField(
                    value = confirmPasswordState.value,
                    label = "Confirm Password",
                    onValueChange = { confirmPasswordState.value = it },
                    isPassword = true
                )
            ),
            onDismiss = { viewModel.hidePasswordModal() },
            onConfirm = { viewModel.updatePassword(currentPasswordState.value, newPasswordState.value, confirmPasswordState.value) },
            isLoading = isUpdatingPassword,
            error = updateError,
            onErrorClear = { viewModel.clearUpdateError() }
        )
    }

    // Delete Account Modal
    if (showDeleteAccountModal) {
        val passwordState = remember { mutableStateOf("") }
        com.example.localshop.core.designsystem.component.InputModal(
            title = "Delete Account",
            fields = listOf(
                com.example.localshop.core.designsystem.component.ModalField(
                    value = passwordState.value,
                    label = "Password",
                    onValueChange = { passwordState.value = it },
                    isPassword = true
                )
            ),
            onDismiss = { viewModel.hideDeleteAccountModal() },
            onConfirm = { viewModel.deleteAccount(passwordState.value) },
            isLoading = isDeletingAccount,
            error = updateError,
            onErrorClear = { viewModel.clearUpdateError() },
            confirmText = "Delete",
            confirmButtonColor = Color.Red,
            showWarning = true,
            warningText = "This action cannot be undone. Please enter your password to confirm."
        )
    }

    // Success Message (Snack/Toast)
    if (updateSuccess != null) {
        androidx.compose.material3.Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                androidx.compose.material3.TextButton(onClick = { viewModel.clearUpdateSuccess() }) {
                    androidx.compose.material3.Text("Dismiss")
                }
            }
        ) {
            androidx.compose.material3.Text(updateSuccess!!)
        }
    }
}

@Composable
private fun NotLoggedInProfileScreen(
    navController: NavController,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile",
            modifier = Modifier.size(120.dp),
            tint = colors.secondaryText.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Welcome to LocalShop",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.primaryText,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Sign in to access your profile, orders, wishlist, and more",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        AppButton(
            text = "Login",
            onClick = { navController.navigate(Screen.Login.route) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AppOutlinedButton(
            text = "Create Account",
            onClick = { navController.navigate(Screen.Register.route) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "By continuing, you agree to our Terms of Service and Privacy Policy",
            style = MaterialTheme.typography.bodySmall,
            color = colors.secondaryText.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoggedInProfileScreen(
    navController: NavController,
    colors: com.example.localshop.core.designsystem.theme.AppColors,
    onLogout: () -> Unit,
    isLoggingOut: Boolean,
    user: com.example.localshop.feature.auth.domain.model.User?,
    viewModel: ProfileViewModel
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
            .verticalScroll(scrollState)
    ) {
        // Header Section - Blue background like Flipkart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.primary)
                .padding(16.dp, 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    if (user?.avatar != null) {
                        AsyncImage(
                            model = user.avatar,
                            contentDescription = "Profile",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(80.dp),
                            tint = colors.primary.copy(alpha = 0.3f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // User Name
                Text(
                    text = "Hello, ${user?.name ?: "User"}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // User Email/Phone
                Text(
                    text = user?.email ?: user?.phone ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Actions Grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileQuickAction(
                icon = Icons.Default.ShoppingBag,
                label = "Orders",
                onClick = { navController.navigate(Screen.OrderHistory.route) },
                colors = colors
            )
            ProfileQuickAction(
                icon = Icons.Default.Favorite,
                label = "Wishlist",
                onClick = { navController.navigate(Screen.Wishlist.route) },
                colors = colors
            )
            ProfileQuickAction(
                icon = Icons.Default.CreditCard,
                label = "Payments",
                onClick = { /* TODO: Navigate to payments */ },
                colors = colors
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Account Settings Section
        ProfileSection(
            title = "Account Settings",
            items = listOf(
                ProfileMenuItem(
                    icon = Icons.Default.AccountCircle,
                    label = "Update Name",
                    onClick = { viewModel.showNameModal() }
                ),
                ProfileMenuItem(
                    icon = Icons.Default.Email,
                    label = "Update Email",
                    onClick = { viewModel.showEmailModal() }
                ),
                ProfileMenuItem(
                    icon = Icons.Default.Phone,
                    label = "Update Phone",
                    onClick = { viewModel.showPhoneModal() }
                ),
                ProfileMenuItem(
                    icon = Icons.Default.CreditCard,
                    label = "Update Password",
                    onClick = { viewModel.showPasswordModal() }
                ),
                ProfileMenuItem(
                    icon = Icons.Default.LocationOn,
                    label = "Manage Addresses",
                    onClick = { navController.navigate(Screen.AddressManagement.route) }
                ),
                ProfileMenuItem(
                    icon = Icons.Default.Help,
                    label = "Help & Support",
                    onClick = { /* TODO: Navigate to help */ }
                ),
                ProfileMenuItem(
                    icon = Icons.Default.Logout,
                    label = "Delete Account",
                    onClick = { viewModel.showDeleteAccountModal() }
                )
            ),
            colors = colors
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.cardBackground
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogout() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = colors.error,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.error,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileQuickAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(60.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = colors.cardBackground
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = colors.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = colors.primaryText,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ProfileSection(
    title: String,
    items: List<ProfileMenuItem>,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = colors.primaryText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.cardBackground
            )
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    ProfileMenuItemRow(
                        icon = item.icon,
                        label = item.label,
                        onClick = item.onClick,
                        colors = colors,
                        showDivider = index < items.size - 1
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    colors: com.example.localshop.core.designsystem.theme.AppColors,
    showDivider: Boolean
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = colors.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.primaryText
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = colors.secondaryText,
                modifier = Modifier.size(20.dp)
            )
        }

        if (showDivider) {
            Divider(
                modifier = Modifier.padding(start = 56.dp),
                color = colors.divider
            )
        }
    }
}

private data class ProfileMenuItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val onClick: () -> Unit
)
